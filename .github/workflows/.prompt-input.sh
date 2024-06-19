#!/bin/bash

# Accept custom message as an argument
CUSTOM_MESSAGE=$1
DEFAULT_VALUE=false

# Start a background process that waits for input
read -t 60 -p "$CUSTOM_MESSAGE (true/false): " user_input || user_input=$DEFAULT_VALUE

# Check if the input is empty and set to default value if true
if [ -z "$user_input" ]; then
  user_input=$DEFAULT_VALUE
fi

# Save the user input to a control file
echo "$user_input" > user_input.txt
