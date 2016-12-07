# -*- coding: utf-8 -*-

# Form implementation generated from reading ui file 'mainwindow.ui'
#
# Created: Wed Dec  7 17:17:18 2016
#      by: PyQt4 UI code generator 4.10.4
#
# WARNING! All changes made in this file will be lost!

from PyQt4 import QtCore, QtGui

try:
    _fromUtf8 = QtCore.QString.fromUtf8
except AttributeError:
    def _fromUtf8(s):
        return s

try:
    _encoding = QtGui.QApplication.UnicodeUTF8
    def _translate(context, text, disambig):
        return QtGui.QApplication.translate(context, text, disambig, _encoding)
except AttributeError:
    def _translate(context, text, disambig):
        return QtGui.QApplication.translate(context, text, disambig)

class Ui_DataToSim(object):
    def setupUi(self, DataToSim):
        DataToSim.setObjectName(_fromUtf8("DataToSim"))
        DataToSim.resize(600, 300)
        sizePolicy = QtGui.QSizePolicy(QtGui.QSizePolicy.Fixed, QtGui.QSizePolicy.Fixed)
        sizePolicy.setHorizontalStretch(0)
        sizePolicy.setVerticalStretch(0)
        sizePolicy.setHeightForWidth(DataToSim.sizePolicy().hasHeightForWidth())
        DataToSim.setSizePolicy(sizePolicy)
        DataToSim.setMinimumSize(QtCore.QSize(600, 300))
        DataToSim.setMaximumSize(QtCore.QSize(600, 300))
        self.centralwidget = QtGui.QWidget(DataToSim)
        sizePolicy = QtGui.QSizePolicy(QtGui.QSizePolicy.Preferred, QtGui.QSizePolicy.Preferred)
        sizePolicy.setHorizontalStretch(0)
        sizePolicy.setVerticalStretch(0)
        sizePolicy.setHeightForWidth(self.centralwidget.sizePolicy().hasHeightForWidth())
        self.centralwidget.setSizePolicy(sizePolicy)
        self.centralwidget.setObjectName(_fromUtf8("centralwidget"))
        self.verticalLayout = QtGui.QVBoxLayout(self.centralwidget)
        self.verticalLayout.setObjectName(_fromUtf8("verticalLayout"))
        self.introLabel = QtGui.QLabel(self.centralwidget)
        sizePolicy = QtGui.QSizePolicy(QtGui.QSizePolicy.Preferred, QtGui.QSizePolicy.Preferred)
        sizePolicy.setHorizontalStretch(0)
        sizePolicy.setVerticalStretch(0)
        sizePolicy.setHeightForWidth(self.introLabel.sizePolicy().hasHeightForWidth())
        self.introLabel.setSizePolicy(sizePolicy)
        self.introLabel.setMinimumSize(QtCore.QSize(0, 80))
        self.introLabel.setMaximumSize(QtCore.QSize(600, 80))
        self.introLabel.setObjectName(_fromUtf8("introLabel"))
        self.verticalLayout.addWidget(self.introLabel)
        spacerItem = QtGui.QSpacerItem(20, 40, QtGui.QSizePolicy.Minimum, QtGui.QSizePolicy.Expanding)
        self.verticalLayout.addItem(spacerItem)
        self.browseLayout = QtGui.QHBoxLayout()
        self.browseLayout.setSpacing(20)
        self.browseLayout.setSizeConstraint(QtGui.QLayout.SetDefaultConstraint)
        self.browseLayout.setObjectName(_fromUtf8("browseLayout"))
        self.label = QtGui.QLabel(self.centralwidget)
        self.label.setMaximumSize(QtCore.QSize(16777215, 30))
        self.label.setObjectName(_fromUtf8("label"))
        self.browseLayout.addWidget(self.label)
        self.pathLineEdit = QtGui.QLineEdit(self.centralwidget)
        self.pathLineEdit.setObjectName(_fromUtf8("pathLineEdit"))
        self.browseLayout.addWidget(self.pathLineEdit)
        self.browseButton = QtGui.QPushButton(self.centralwidget)
        self.browseButton.setObjectName(_fromUtf8("browseButton"))
        self.browseLayout.addWidget(self.browseButton)
        self.verticalLayout.addLayout(self.browseLayout)
        spacerItem1 = QtGui.QSpacerItem(20, 20, QtGui.QSizePolicy.Minimum, QtGui.QSizePolicy.Expanding)
        self.verticalLayout.addItem(spacerItem1)
        self.bottomLayout = QtGui.QHBoxLayout()
        self.bottomLayout.setObjectName(_fromUtf8("bottomLayout"))
        spacerItem2 = QtGui.QSpacerItem(40, 20, QtGui.QSizePolicy.Expanding, QtGui.QSizePolicy.Minimum)
        self.bottomLayout.addItem(spacerItem2)
        self.closeButton = QtGui.QPushButton(self.centralwidget)
        self.closeButton.setObjectName(_fromUtf8("closeButton"))
        self.bottomLayout.addWidget(self.closeButton)
        self.nextButton = QtGui.QPushButton(self.centralwidget)
        self.nextButton.setObjectName(_fromUtf8("nextButton"))
        self.bottomLayout.addWidget(self.nextButton)
        self.verticalLayout.addLayout(self.bottomLayout)
        DataToSim.setCentralWidget(self.centralwidget)

        self.retranslateUi(DataToSim)
        QtCore.QMetaObject.connectSlotsByName(DataToSim)

    def retranslateUi(self, DataToSim):
        DataToSim.setWindowTitle(_translate("DataToSim", "MainWindow", None))
        self.introLabel.setText(_translate("DataToSim", "<html><head/><body><p>Select the data file that you want to load into the flight simulator in the following bar. </p><p>Data will be used to start a simple simulation with the acquired parameters.</p></body></html>", None))
        self.label.setText(_translate("DataToSim", "File path :", None))
        self.browseButton.setText(_translate("DataToSim", "Browse...", None))
        self.closeButton.setText(_translate("DataToSim", "Close", None))
        self.nextButton.setText(_translate("DataToSim", "Next", None))

