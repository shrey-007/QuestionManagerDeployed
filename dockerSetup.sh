#!/bin/bash

# Step 1: Remove specified Docker-related packages
for pkg in docker.io docker-doc docker-compose docker-compose-v2 podman-docker containerd runc; do
    sudo apt-get remove -y $pkg
done

# Step 2: Update package list
sudo apt-get update

# Step 3: Install ca-certificates and curl
sudo apt-get install -y ca-certificates curl

# Step 4: Create the directory for Docker keyring
sudo install -m 0755 -d /etc/apt/keyrings

# Step 5: Download Docker's GPG key
sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc

# Step 6: Ensure the GPG key is readable
sudo chmod a+r /etc/apt/keyrings/docker.asc

# Step 7: Add Docker's repository to APT sources
echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu \
  $(. /etc/os-release && echo "$VERSION_CODENAME") stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

# Step 8: Update package list again
sudo apt-get update

# Step 9: Install Docker packages
sudo apt-get install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
