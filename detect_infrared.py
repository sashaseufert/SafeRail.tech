# -*- coding: utf-8 -*-
"""
Created on Sat Feb  3 18:07:03 2018

@author: Sacha Perry-Fagant
"""

import numpy as np
import cv2
import matplotlib.pyplot as plt
from PIL import Image
import glob, os

# Size threshhold (pixels)
size = 400
# Brightness threshhold
magnitude = 200

viewImage = True

def load_images():
    os.chdir("infrared/")
    imgs = []
    for filename in glob.glob("*.jpg"):
        img = cv2.imread(filename,0)
        
        for i in range(len(img)):
            for j in range(len(img[i])):
                val = img[i][j]
                if val > magnitude:
                    img[i][j] = 255
                else:
                    img[i][j] = 0
        if viewImage:
            cv2.imshow('image',img)
            cv2.waitKey(0)
            cv2.destroyAllWindows()
            
        imgs.append(img)
        
    return imgs

def detect_clusters(img, viewImage = False):    
    contours,hierarchy= cv2.findContours(img,cv2.RETR_TREE,cv2.CHAIN_APPROX_SIMPLE)
    if viewImage:
        cv2.drawContours(img, contours, -1, (255, 255, 255))
        cv2.imshow('image',img)
        cv2.waitKey(0)
        cv2.destroyAllWindows()    
        
    maxArea = 0

    for c in contours:
        area = cv2.contourArea(c)
        if area > maxArea:
            maxArea = area
    return maxArea

images = load_images()
is_animal = False
for image in images:
    area = detect_clusters(image, True)
    if area > size:
        print("There is an animal on the tracks")
        is_animal = True

remove_files = False
if remove_files:
    for filename in glob.glob("*.jpg"):
        os.remove(filename)

