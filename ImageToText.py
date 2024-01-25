"""
Author: Aditya Bankoti
Date: December 5th, 2023

Description:
This script reads an image ('Pac-Man.png'), processes it to identify certain patterns,
and displays the result. The processed image is printed as a grid with dots ('.') and walls ('W').
"""

import cv2
import numpy as np
import scipy.ndimage as scipy

# Define grid dimensions
width = 21
height = 21

# Read the image
img = cv2.imread('Pac-Man.png')
imgH, imgW, channels = img.shape

# Calculate box dimensions based on grid
boxW = int(imgW / width)
boxH = int(imgH / height)

gx = 0
gy = 0

# Initialize a 2D array to store the grid information
total = [["W"] * width for _ in range(height)]

# Iterate over the grid
for gy in range(0, width):
    r = []
    for gx in range(0, height):
        x = gx * boxW
        y = gy * boxH
        values = img[x: x + boxW, y: y + boxH]

        # Find unique colors in the box
        colors = np.unique(values, axis=0, return_counts=False)
        dot = False

        # Check for the presence of a white pixel (255, 255, 255)
        for row in colors:
            if dot:
                break
            for pixel in row:
                cv2.rectangle(img, (y, x), (y + boxW, x + boxH), color=(0, 255, 0), thickness=1)
                if np.all(pixel == 255):
                    dot = True
                    break

        # Update the grid based on the presence of a white pixel
        if dot:
            cv2.rectangle(img, (y, x), (y + boxW, x + boxH), color=(0, 255, 255), thickness=2)
            total[gx][gy] = '.'
            r.append(".")
        else:
            r.append("W")

    # Update the total array
    total.append(r)

# Rotate and print the grid
rotate = [r[::-1][height:] for r in list(zip(*total[::-1]))][:width]

for row in rotate:
    for box in row:
        print(box, end="")
    print()

# Display the processed image
cv2.imshow('image', img)
cv2.waitKey(0)
cv2.destroyAllWindows()
