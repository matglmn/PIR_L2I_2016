from PyQt4 import QtGui, QtCore
import os
import sys
import itertools
import json
import pdb
from pprint import pprint


def parseDataFile(data_file):
    with open(data_file) as f:
        data = json.load(f)
    return data


class DataWizard(QtGui.QWizard):
    def __init__(self, parent=None):
        super(DataWizard, self).__init__(parent)

        self.setWindowTitle("Data to Sim")

        self.addPage(Page1())
        self.addPage(Page2())
        self.addPage(Page3())


class Page1(QtGui.QWizardPage):
    def __init__(self, parent=None):
        super(Page1, self).__init__(parent)

        self.setObjectName("Page1")

        self.pathLineEdit = QtGui.QLineEdit()
        self.pathLabel = QtGui.QLabel()
        self.browseButton = QtGui.QPushButton()
        self.introLabel = QtGui.QLabel()

        self.pathLabel.setText("File path :")
        self.introLabel.setText("Welcome on Data to Sim. You can use this application to run acquired data into a simulator !"
                           "\n\nSelect the data file that you want to load into the flight simulator in the following bar. "
                           "\nData will be used to start a simple simulation with the acquired parameters.")
        self.browseButton.setText("Browse...")

        vertspacer1 = QtGui.QSpacerItem(20, 50)
        vertspacer2 = QtGui.QSpacerItem(20, 50)


        horizlayout = QtGui.QHBoxLayout()
        horizlayout.addWidget(self.pathLabel)
        horizlayout.addWidget(self.pathLineEdit)
        horizlayout.addWidget(self.browseButton)

        vertlayout = QtGui.QVBoxLayout()
        vertlayout.addItem(vertspacer1)
        vertlayout.addWidget(self.introLabel)
        vertlayout.addItem(vertspacer2)
        vertlayout.addLayout(horizlayout)

        self.setLayout(vertlayout)

        self.registerField("path*", self.pathLineEdit)
        self.browseButton.clicked.connect(self.selectFile)


    def selectFile(self):
        self.datafile_path = QtGui.QFileDialog.getOpenFileName(self, 'Open data file', os.getenv("HOME"),
                                                                    "Text files (*.json *.txt)")
        self.pathLineEdit.setText(self.datafile_path)


class Page2(QtGui.QWizardPage):
    def __init__(self, parent=None):
        super(Page2, self).__init__(parent)

        self.setObjectName("Page2")
        self.page_name = self.objectName()

        self.vertlayout = QtGui.QVBoxLayout()
        self.overtextSpacer = QtGui.QSpacerItem(40, 40)
        self.vertlayout.addItem(self.overtextSpacer)
        self.overlabel = QtGui.QLabel()
        self.overlabel.setText("Select the marker you want to replay on simulator :")
        self.vertlayout.addWidget(self.overlabel)
        self.subtextSpacer = QtGui.QSpacerItem(20, 20)
        self.vertlayout.addItem(self.subtextSpacer)
        self.data_list = QtGui.QListWidget()
        self.registerField("id_obj", self.data_list)
        self.vertlayout.addWidget(self.data_list)
        self.setLayout(self.vertlayout)

    def initializePage(self):
        self.file_path = str(self.field("path"))
        self.data_obj= parseDataFile(self.file_path)
        self.setCheckBoxes(self.data_obj)

    def setCheckBoxes(self, data):
        for i in range(len(data)):
            self.data_item = QtGui.QListWidgetItem()
            self.data_item.setText(str(i+1) + " - " + data[i]["date"])
            self.data_list.addItem(self.data_item)


class Page3(QtGui.QWizardPage):
    def __init__(self, parent=None):
        super(Page3, self).__init__(parent)

        self.vertlayout = QtGui.QVBoxLayout()
        self.overlabel = QtGui.QLabel()
        self.overlabel.setText('Here are the parameters that will be loaded into the flight simulator. '
                               '\nClick on "Finish" button to launch simulation')
        self.latitude_label = QtGui.QLabel()
        self.latitude_label.setText("Latitude (°):")
        self.latitude_edit = QtGui.QLineEdit()
        self.longitude_label = QtGui.QLabel()
        self.longitude_label.setText("Longitude (°):")
        self.longitude_edit = QtGui.QLineEdit()
        self.altitude_label = QtGui.QLabel()
        self.altitude_label.setText("Altitude (m):")
        self.altitude_edit = QtGui.QLineEdit()
        self.speed_label = QtGui.QLabel()
        self.speed_label.setText("Speed (m/s):")
        self.speed_edit = QtGui.QLineEdit()
        self.roll_label = QtGui.QLabel()
        self.roll_label.setText("Roll angle (°):")
        self.roll_edit = QtGui.QLineEdit()
        self.pitch_label = QtGui.QLabel()
        self.pitch_label.setText("Pitch angle (°):")
        self.pitch_edit = QtGui.QLineEdit()

        self.vertlayout.addWidget(self.overlabel)
        self.vertlayout.addWidget(self.latitude_label)
        self.vertlayout.addWidget(self.latitude_edit)
        self.vertlayout.addWidget(self.longitude_label)
        self.vertlayout.addWidget(self.longitude_edit)
        self.vertlayout.addWidget(self.altitude_label)
        self.vertlayout.addWidget(self.altitude_edit)
        self.vertlayout.addWidget(self.speed_label)
        self.vertlayout.addWidget(self.speed_edit)
        self.vertlayout.addWidget(self.roll_label)
        self.vertlayout.addWidget(self.roll_edit)
        self.vertlayout.addWidget(self.pitch_label)
        self.vertlayout.addWidget(self.pitch_edit)

        self.setLayout(self.vertlayout)

    def initializePage(self):
        self.file_path = self.field("path")
        data_obj = parseDataFile(self.file_path)
        data_id = self.field("id_obj")
        self.latitude_edit.setText(str(data_obj[data_id]["latitude"]))
        self.longitude_edit.setText(str(data_obj[data_id]["longitude"]))
        self.altitude_edit.setText(str(data_obj[data_id]["altitude"]))
        self.speed_edit.setText(str(data_obj[data_id]["speed"]))
        self.roll_edit.setText(str(data_obj[data_id]["roll"]))
        self.pitch_edit.setText(str(data_obj[data_id]["pitch"]))

if __name__ == '__main__':
    app = QtGui.QApplication(sys.argv)
    wizard = DataWizard()
    wizard.show()
    sys.exit(app.exec_())