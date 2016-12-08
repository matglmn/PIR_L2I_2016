from PyQt4 import QtGui
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

        self.registerField("path*", self.pathLineEdit)
        self.browseButton.clicked.connect(self.selectFile)


    def selectFile(self):
        self.datafile_path = QtGui.QFileDialog.getOpenFileName(self, 'Open data file', os.getenv("HOME"),
                                                                    "Text files (*.txt *.data *.sav *.log)")
        self.pathLineEdit.setText(self.datafile_path)


class Page2(QtGui.QWizardPage):
    def __init__(self, parent=None):
        super(Page2, self).__init__(parent)
        self.vertlayout = QtGui.QVBoxLayout()
        self.setLayout(self.vertlayout)

    def initializePage(self):
        self.file_path = str(self.field("path"))
        self.file_lines = self.parseDataFile(self.file_path)
        self.setCheckBoxes(self.file_lines)

    def setCheckBoxes(self, lines):
        for i in range(len(lines)):
            self.choice = QtGui.QRadioButton()
            self.choice.setText(str(lines[i]))
            self.vertlayout.addWidget(self.choice)


    def parseDataFile(self, data_file):
        with open(data_file) as f:
            lines = [line.rstrip('\n') for line in f]

        return lines


if __name__ == '__main__':
    app = QtGui.QApplication(sys.argv)
    wizard = DataWizard()
    wizard.show()
    sys.exit(app.exec_())