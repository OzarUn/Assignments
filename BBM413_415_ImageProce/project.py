import sys
import os
import cv2
import numpy as np
from PyQt5.QtWidgets import *
from PyQt5.QtGui import QImage, QPixmap, QPalette, QColor
from PyQt5 import QtGui
from PIL import Image
from PIL.ImageQt import ImageQt
from PyQt5.QtCore import Qt
from PIL import Image, ImageOps
from PIL import Image, ImageEnhance

class AnaPencere(QMainWindow):
    def __init__(self):
        super().__init__()
        self.controller = [False,False,False,False,False,False,False,False,False,False,False,False]
        self.widget = QWidget(self)
        layout = QHBoxLayout()
        self.label = QLabel()
        self.label2 = QLabel()
        self.grid = QGridLayout()
        self.setWindowTitle("Project")

        layout.addWidget(self.label)
        layout.addWidget(self.label2)
        widget = QWidget()
        widget.setLayout(layout)
        self.setCentralWidget(widget)

#menu bar part
        self.menuBar = QMenuBar(self)
        self.setMenuBar(self.menuBar)
        self.menu = QMenu(self.menuBar)
        self.menu.setTitle("File")
        self.menuBar.addMenu(self.menu)
        self.action1 = QAction(self.menu)
        self.action1.setText("Load Image")
        self.action1.triggered.connect(self.loadFile)

        self.menu.addAction(self.action1)
        self.menu.addSeparator()
        self.action2 = QAction(self.menu)
        self.action2.setText("Save Image")
        self.menu.addAction(self.action2)
        self.action2.triggered.connect(self.saveFile)

        self.menu2 = QMenu(self.menuBar)
        self.menu2.setTitle("Edit")
        self.menuBar.addMenu(self.menu2)

        self.menu2.addSeparator()
        self.action10 = QAction(self.menu2)
        self.action10.setText("Blur Image")
        self.menu2.addAction(self.action10)
        self.action10.triggered.connect(self.blur)
        
        self.menu2.addSeparator()
        self.action10 = QAction(self.menu2)
        self.action10.setText("Deblur Image")
        self.menu2.addAction(self.action10)
        self.action10.triggered.connect(self.deblur)

        self.menu2.addSeparator()
        self.action3 = QAction(self.menu2)
        self.action3.setText("Grayscale image")
        self.menu2.addAction(self.action3)
        self.action3.triggered.connect(self.grayscale_image)

        self.menu2.addSeparator()
        self.action4 = QAction(self.menu2)
        self.action4.setText("Crop image")
        self.menu2.addAction(self.action4)
        self.action4.triggered.connect(self.crop_image)

        self.menu2.addSeparator()
        self.action6 = QAction(self.menu2)
        self.action6.setText("Mirror image")
        self.menu2.addAction(self.action6)
        self.action6.triggered.connect(self.miror_image)

        self.menu2.addSeparator()
        self.action10 = QAction(self.menu2)
        self.action10.setText("Flip image")
        self.menu2.addAction(self.action10)
        self.action10.triggered.connect(self.flip_image)

        self.menu2.addSeparator()
        self.action11 = QAction(self.menu2)
        self.action11.setText("Rotate image 90*")
        self.menu2.addAction(self.action11)
        self.action11.triggered.connect(self.rotate_image)

        self.menu2.addSeparator()
        self.action7 = QAction(self.menu2)
        self.action7.setText("Reverse the color of image")
        self.menu2.addAction(self.action7)
        self.action7.triggered.connect(self.reverse_the_color)

        self.menu2.addSeparator()
        self.action12 = QAction(self.menu2)
        self.action12.setText("Change color balance")
        self.menu2.addAction(self.action12)
        self.action12.triggered.connect(self.change_color_bal)

        self.menu2.addSeparator()
        self.action12 = QAction(self.menu2)
        self.action12.setText("Adjust brightness of image")
        self.menu2.addAction(self.action12)
        self.action12.triggered.connect(self.adjust_brightness)

        self.menu2.addSeparator()
        self.action13 = QAction(self.menu2)
        self.action13.setText("Adjust contrast of image")
        self.menu2.addAction(self.action13)
        self.action13.triggered.connect(self.adjust_contrast)

        self.menu2.addSeparator()
        self.action14 = QAction(self.menu2)
        self.action14.setText("Adjust saturation of image")
        self.menu2.addAction(self.action14)
        self.action14.triggered.connect(self.adjust_saturation)

        self.menu2.addSeparator()
        self.action8 = QAction(self.menu2)
        self.action8.setText("Detect edges of image")
        self.menu2.addAction(self.action8)
        self.action8.triggered.connect(self.edge_detect)

        self.menu2.addSeparator()
        self.action9 = QAction(self.menu2)
        self.action9.setText("Add noise to image")
        self.menu2.addAction(self.action9)
        self.action9.triggered.connect(self.add_noise_to_image)

    def loadFile(self):
        fname, _ = QFileDialog.getOpenFileName(self, 'Open file','c:\\',"Image files (*.jpg *.gif)")
        image = cv2.imread(fname)
        self.cv_image = image
        self.cv_image_backup = image
        if image is not None:
            pix = self.convert_cv_qt(image)
            self.label.setPixmap(pix)
            self.label2.setPixmap(pix)
        else:
            print("Nothing Selected")
    def saveFile(self):
        fname = QFileDialog.getSaveFileName(self, 'Save file', 'c:\\', "Image files (*.jpg *.gif *.png)" )
        try:
            cv2.imwrite(fname[0], self.cv_image)
            msg=QMessageBox()
            msg.setWindowTitle("Done")
            msg.setText("Your image saved to :\n"+ fname[0])
            msg.exec_()
        except Exception as e:
            print(str(e))


    def convert_cv_qt(self, cv_img):
        rgb_image = cv2.cvtColor(cv_img, cv2.COLOR_BGR2RGB)
        h, w, ch = rgb_image.shape
        bytes_per_line = ch * w
        convert_to_Qt_format = QtGui.QImage(rgb_image.data, w, h, bytes_per_line, QtGui.QImage.Format_RGB888)
        p = convert_to_Qt_format.scaled(500, 500, Qt.KeepAspectRatio)
        return QPixmap.fromImage(p)

#problem reverting grayscale image
    def grayscale_image(self):
        # if self.controller[0] is False:
            self.cv_image_backup = self.cv_image
            gray = cv2.cvtColor(self.cv_image, cv2.COLOR_BGR2GRAY)
            self.cv_image = gray
            gray_last = self.convert_cv_qt(gray)
            self.label2.setPixmap(gray_last)
            self.controller[0] = True
        # else:
        #     self.cv_image = self.cv_image_backup
        #     last = self.convert_cv_qt(self.cv_image)
        #     self.label2.setPixmap(last)
        #     self.controller[0] = False

    def blur (self):
        blurImg = cv2.blur(self.cv_image,(10,10)) 
        self.cv_image = blurImg
        blur_last = self.convert_cv_qt(blurImg)
        self.label2.setPixmap(blur_last)

    def deblur (self):
        window = np.array([[-1,-1,-1], [-1,9,-1], [-1,-1,-1]])
        image_sharp = cv2.filter2D(self.cv_image,ddepth=-1,kernel=window)
        self.cv_image = image_sharp
        sharp_last= self.convert_cv_qt(image_sharp)
        self.label2.setPixmap(sharp_last)

    def crop_image(self):
        x1 = QLineEdit()
        y1 = QLineEdit()
        x2 = QLineEdit()
        y2 = QLineEdit()
        dll = QDialog(self)
        layoutFor = QFormLayout(dll)
        tup = ("Coordinates must be smaller than (",str(self.cv_image.shape[1]),"),",str(self.cv_image.shape[0]),"\n !! x1 < x2 && y1 < y2")
        layoutFor.addRow(QLabel(''.join(tup)))
        layoutFor.addRow(QLabel("X1"), x1) 
        layoutFor.addRow(QLabel("Y1"), y1)
        layoutFor.addRow(QLabel("X2"), x2) 
        layoutFor.addRow(QLabel("Y2"), y2)
        hlayout = QHBoxLayout()
        self.bt = QPushButton("Okay")
        self.bt.setCheckable(True)
        self.bt.toggle()
        self.bt.clicked.connect(dll.close)
        hlayout.addWidget(self.bt)
        layoutFor.addRow(hlayout)
        dll.exec()        
        last_im = self.cv_image[int(x1.text()):int(x2.text()),int(y1.text()):int(y2.text())]
        self.cv_image = last_im
        laste = self.convert_cv_qt(last_im)
        self.label2.setPixmap(laste)

    def miror_image(self):
        flipBoth = cv2.flip(self.cv_image, 1)
        self.cv_image = flipBoth
        pixLas = self.convert_cv_qt(flipBoth)
        self.label2.setPixmap(pixLas)

    def flip_image(self):
        flipBoth = cv2.flip(self.cv_image, 0)
        self.cv_image = flipBoth
        pixLas = self.convert_cv_qt(flipBoth)
        self.label2.setPixmap(pixLas)

    def rotate_image(self):
        last_im = cv2.rotate(self.cv_image,cv2.ROTATE_90_CLOCKWISE)
        self.cv_image = last_im 
        rotlas = self.convert_cv_qt(last_im)
        self.label2.setPixmap(rotlas)

    def reverse_the_color(self):
        imageRev = ~(self.cv_image)
        self.cv_image = imageRev
        last_rev = self.convert_cv_qt(imageRev)
        self.label2.setPixmap(last_rev)

    def change_color_bal(self):
        newR = QLineEdit()
        newG = QLineEdit()
        newB = QLineEdit()
        dlf = QDialog(self)
        layoutFor = QFormLayout(dlf)
        layoutFor.addRow(QLabel("Red"), newR) 
        layoutFor.addRow(QLabel("Green"), newG) 
        layoutFor.addRow(QLabel("Blue"), newB) 
        hlayout = QHBoxLayout()
        self.bt = QPushButton("Okay")
        self.bt.setCheckable(True)
        self.bt.toggle()
        self.bt.clicked.connect(dlf.close)
        hlayout.addWidget(self.bt)
        layoutFor.addRow(hlayout)
        dlf.exec()        
        #Red
        self.cv_image[:,:,2] = self.cv_image[:,:,2] * (int(newR.text())*100)
        #Green
        self.cv_image[:,:,1] = self.cv_image[:,:,1] * (int(newG.text())*100)
        #Blue
        self.cv_image[:,:,0] = self.cv_image[:,:,0] * (int(newB.text())*100)
        last_bal = self.convert_cv_qt(self.cv_image)
        self.label2.setPixmap(last_bal)
        
    def adjust_brightness(self):
        img = cv2.cvtColor(self.cv_image, cv2.COLOR_BGR2RGB)
        im_pil = Image.fromarray(img)
        dlg = QDialog(self)
        layoutFor = QFormLayout(dlg)
        layoutFor.addRow(QLabel("Adjust")) 
        hlayout = QHBoxLayout()
        slider = QSlider(Qt.Horizontal)
        slider.setValue(50)
        slider.setMinimum(0)
        slider.setMaximum(100)
        hlayout.addWidget(slider)
        layoutFor.addRow(hlayout) 
        dlg.exec()
        #bright function
        enhancer = ImageEnhance.Brightness(im_pil)
        im_output = enhancer.enhance(slider.value()/50)
        im_np = np.asarray(im_output)
        self.cv_image = cv2.cvtColor(im_np,cv2.COLOR_RGB2BGR)
        im_np = self.cv_image

        last = self.convert_cv_qt(im_np)
        self.label2.setPixmap(last)

    def adjust_contrast(self):
        img = cv2.cvtColor(self.cv_image, cv2.COLOR_BGR2RGB)
        im_pil = Image.fromarray(img)
        dlg = QDialog(self)
        layoutFor = QFormLayout(dlg)
        layoutFor.addRow(QLabel("Adjust")) 
        hlayout = QHBoxLayout()
        slider = QSlider(Qt.Horizontal)
        slider.setValue(50)
        slider.setMinimum(0)
        slider.setMaximum(100)
        hlayout.addWidget(slider)
        layoutFor.addRow(hlayout) 
        dlg.exec()
        #contrast function
        image = ImageEnhance.Contrast(im_pil).enhance(slider.value()/50)

        im_np = np.asarray(image)
        self.cv_image = cv2.cvtColor(im_np,cv2.COLOR_RGB2BGR)
        im_np = self.cv_image
        last = self.convert_cv_qt(im_np)
        self.label2.setPixmap(last)

    def adjust_saturation(self):
        img = cv2.cvtColor(self.cv_image, cv2.COLOR_BGR2RGB)
        im_pil = Image.fromarray(img)
        dlg = QDialog(self)
        layoutFor = QFormLayout(dlg)
        layoutFor.addRow(QLabel("Adjust")) 
        hlayout = QHBoxLayout()
        slider = QSlider(Qt.Horizontal)
        slider.setValue(50)
        slider.setMinimum(0)
        slider.setMaximum(100)
        hlayout.addWidget(slider)
        layoutFor.addRow(hlayout) 
        dlg.exec()
        #Saturation function
        ench = ImageEnhance.Color(im_pil)
        last = ench.enhance(slider.value()/50)

        im_np = np.asarray(last)
        self.cv_image = cv2.cvtColor(im_np,cv2.COLOR_RGB2BGR)
        im_np = self.cv_image
        last = self.convert_cv_qt(im_np)
        self.label2.setPixmap(last)

    def add_noise_to_image(self):
        # if self.controller[10] is False:
            self.cv_image_backup = self.cv_image        
            gaus = np.random.normal(0,1,self.cv_image.size)
            gaus = gaus.reshape(self.cv_image.shape[0],self.cv_image.shape[1],self.cv_image.shape[2]).astype('uint8')
            img_gaus = cv2.add(self.cv_image,gaus)
            self.cv_image = img_gaus
            last_gaus = self.convert_cv_qt(img_gaus)
            self.label2.setPixmap(last_gaus)
            self.controller[10] = True
        # else:
        #     self.cv_image = self.cv_image_backup
        #     last = self.convert_cv_qt(self.cv_image)
        #     self.label2.setPixmap(last)
        #     self.controller[10] = False
    def edge_detect(self):
        edges = cv2.Canny(self.cv_image, threshold1=100, threshold2=200)
        self.cv_image = edges
        last_edges = self.convert_cv_qt(edges)
        self.label2.setPixmap(last_edges)
        





uygulama = QApplication(sys.argv)
pencere = AnaPencere()
pencere.show()
uygulama.exec_()


