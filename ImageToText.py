import cv2
import numpy as np
import scipy.ndimage as scipy

width = 21
height = 21

# Read the image
img = cv2.imread('Pac-Man.png')
imgH, imgW, channels = img.shape

boxW = int(imgW / width)
boxH = int(imgH / height)

gx = 0
gy = 0

total = [[ "W" ] * width] * height

for gy in range(0, width):
    r = []
    for gx in range(0, height):
        x = gx * boxW
        y = gy * boxH
        values = img[x : x + boxW, y : y + boxH]
        colors = np.unique(values, axis = 0, return_counts = False)
        dot = False
        for row in colors:
            if dot:
                break
            for pixel in row:
                cv2.rectangle(img, (y, x), (y + boxW, x + boxH), color=(0, 255, 0), thickness=1)
                if np.all(pixel == 255):
                    # cv2.rectangle(img, (y, x), (y + boxW, x + boxH), color=(0, 255, 255), thickness=2)
                    # print(x, y)
                    dot = True
                    break
        if dot:
            cv2.rectangle(img, (y, x), (y + boxW, x + boxH), color=(0, 255, 255), thickness=2)
            # print(dot)
            total[gx][gy] = '.'
            r.append(".")
        else:
            r.append("W")
            # total[gx][gy] = 'W'
    total.append(r)

# for x in range(0, width):
#     for y in range(height, 0, -1):
#         print(total[y][x], end="")
#     print()

rotate = [ r[::-1][height:] for r in list(zip(*total[::-1])) ][:width]
print(len(rotate[0]))

for row in rotate:
    for box in row:
        print(box, end="")
    print()
# display image
cv2.imshow('image', img)


cv2.waitKey(0)
cv2.destroyAllWindows()
