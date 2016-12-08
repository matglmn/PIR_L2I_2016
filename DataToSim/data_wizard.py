from PyQt4 import QtGui, QtCore
import os
import sys


class DataWizard(QtGui.QWizard):
    def __init__(self, parent=None):
        super(DataWizard, self).__init__(parent)

        self.setWindowTitle("Data to Sim")
        self.addPage(Page1())
        self.addPage(Page2())


class Page1(QtGui.QWizardPage):
    def __init__(self, parent=None):
        super(Page1, self).__init__(parent)

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

        self.registerField("*", self.pathLineEdit)
        self.browseButton.clicked.connect(self.selectFile)

        print(self.getDataFile())


    def selectFile(self):
        self.datafile_path = QtGui.QFileDialog.getOpenFileName(self, 'Open data file', os.getenv("HOME"),
                                                                    "Text files (*.txt *.data *.sav *.log)")
        self.pathLineEdit.setText(self.datafile_path)


    def getDataFile(self):
        self.datafile_path = self.pathLineEdit.text()
        return self.datafile_path


class Page2(QtGui.QWizardPage):
    def __init__(self, parent=None):
        super(Page2, self).__init__(parent)

        self.data_file = Page1.getDataFile(self)
        self.parseDataFile(self.data_file)

    def parseDataFile(self, data_file):
        with open(self.data_file) as f:
            lines = [line.rstrip('\n') for line in f]

        return lines






if __name__ == '__main__':
    app = QtGui.QApplication(sys.argv)
    wizard = DataWizard()
    wizard.show()
    sys.exit(app.exec_())