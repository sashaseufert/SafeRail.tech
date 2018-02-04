# -*- coding: utf-8 -*-
"""
Created on Sat Feb 03 21:01:59 2018

@author: Sacha Perry-Fagant

Resources:
https://www.pyimagesearch.com/2016/04/11/finding-extreme-points-in-contours-with-opencv/

"""

import cv2
import math
import glob, os

viewImage = True
lenThresh = 10


def find_max(img):
    m = 0
    for row in img:
        for col in row:
            if col > m:
                m = col
    return m

# Finds the brightest pixel and uses this as the target value
# Loads in all the image files
def load_images_brightest(filename, thresh = 60):
    os.chdir("UV/")
    imgs = []
    for filename in glob.glob("*.jpg"):
        img = cv2.imread(filename,0)
        max_val = find_max(img)
    
        for i in range(len(img)):
            for j in range(len(img[i])):
                val = img[i][j]
                if val < max_val - thresh:
                    img[i][j] = 0
        if viewImage:
            cv2.imshow('image', img)
            cv2.waitKey(0)
            cv2.destroyAllWindows()
        imgs.append(img)
    return imgs

def detect_clusters(img, thresh):    
    contours,hierarchy= cv2.findContours(img,cv2.RETR_TREE,cv2.CHAIN_APPROX_SIMPLE)
    if viewImage:
        cv2.drawContours(img, contours, -1, (255, 255, 255))
        cv2.imshow('image',img)
        cv2.waitKey(0)
        cv2.destroyAllWindows()    
    maxL = 0
    maxC = contours[0]
    no_cracks = True
    for c in contours:
        area = cv2.contourArea(c)
        leng = lengh_over_area(c)
        
        if leng > 0 and area/leng < thresh and leng > lenThresh:
            if leng > maxL:
                maxL = leng
                maxC = c
                no_cracks = False
    return maxL, maxC, no_cracks

# contour c
def lengh_over_area(c):
    extLeft = tuple(c[c[:, :, 0].argmin()][0])
    extRight = tuple(c[c[:, :, 0].argmax()][0])
    extTop = tuple(c[c[:, :, 1].argmin()][0])
    extBot = tuple(c[c[:, :, 1].argmax()][0])
    wid = math.sqrt((extLeft[0] - extRight[0])**2+(extLeft[1]-extRight[1])**2)
    hei = math.sqrt((extTop[0] - extBot[0])**2+(extTop[1]-extBot[1])**2)    
    val = max([wid,hei])
    
    return val

# Shows the largest contour if there exists a crack
def show_max_contour(c, image):
    extLeft = tuple(c[c[:, :, 0].argmin()][0])
    extRight = tuple(c[c[:, :, 0].argmax()][0])
    extTop = tuple(c[c[:, :, 1].argmin()][0])
    extBot = tuple(c[c[:, :, 1].argmax()][0])
    
    if viewImage:
        cv2.drawContours(image, [c], -1, (0, 255, 255), 2)
        cv2.circle(image, extLeft, 8, (0, 0, 255), -1)
        cv2.circle(image, extRight, 8, (0, 255, 0), -1)
        cv2.circle(image, extTop, 8, (255, 0, 0), -1)
        cv2.circle(image, extBot, 8, (255, 255, 0), -1)
     
    # show the output image
    cv2.imshow("Image", image)
    cv2.waitKey(0)



def do_everything():
    images = load_images_brightest(50)
    is_cracks = False
    for image in images:
        L, C, no_cracks = detect_clusters(image, 10)
        
        if not no_cracks:
            is_cracks = True
            if viewImage:
                show_max_contour(C, image)
    remove_files = False
    if remove_files:
        for filename in glob.glob("*.jpg"):
            os.remove(filename)

    return is_cracks


if __name__ == "__main__":
    do_everything()
    
