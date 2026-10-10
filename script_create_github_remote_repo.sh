#!/bin/bash

# Prompt the user for input
read -p "Enter the repository name: " REPO_NAME
read -p "Enter your GitHub username: " GITHUB_USERNAME
# read -sp "Enter your GitHub personal access token: " GITHUB_TOKEN
# echo
# read -p "Enter the local directory path: " LOCAL_DIR

# Step 1: Create a GitHub repository using the GitHub API
echo "Creating GitHub repository..."

RESPONSE=$(curl -s -w "%{http_code}" \
  -X POST \
  -H "Authorization: token $GITHUB_TOKEN" \
  -H "Accept: application/vnd.github+json" \
  -d "{\"name\": \"$REPO_NAME\", \"private\": false}" \
  https://api.github.com/user/repos)

BODY=$(echo "$RESPONSE" | head -n1)
CREATE_STATUS=$(echo "$RESPONSE" | tail -n1)

# SUCCESS (2xx)
if [[ "$CREATE_STATUS" =~ ^2 ]]; then
  echo "Repository created successfully."

# ALREADY EXISTS
elif [[ "$CREATE_STATUS" == "422" ]]; then
  echo "Remote repository already exists."

  read -p "Do you want to continue anyway? (y/n): " CONTINUE_CHOICE

  if [[ "$CONTINUE_CHOICE" != "y" && "$CONTINUE_CHOICE" != "Y" ]]; then
    echo "Script terminated by user."
    exit 0
  fi

  echo "Continuing..."

# ERROR (4xx / 5xx except 422)
else
  echo "ERROR: Failed to create remote repository."
  echo "GitHub API returned HTTP code: $CREATE_STATUS"

  echo
  echo "Response from GitHub API:"
  echo "$BODY"

  exit 1
fi

# Step 2: Navigate to the local directory
# cd "$LOCAL_DIR" || exit

# Step 3: Initialize Git locally (if not already done)
if [ ! -d ".git" ]; then
  echo "Initializing Git repository..."
  git init
fi

# Step 4: Add all files and commit
echo "Adding files and committing..."
git add .

if git diff --cached --quiet; then
  echo "No changes to commit."
else
  git commit -m "Initial commit"
fi

# Step 5: Add the remote repository
echo "Adding remote repository..."

git remote add origin https://github.com/$GITHUB_USERNAME/$REPO_NAME.git 2>/dev/null || \
git remote set-url origin https://github.com/$GITHUB_USERNAME/$REPO_NAME.git

# Step 6: Push the code to GitHub
echo "Pushing code to GitHub..."
git branch -M main
git push -u origin main

echo "Repository setup complete!"