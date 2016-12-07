import mainwindow as ui
import sys
import os
from PyQt4 import QtGui


class DataToSim(QtGui.QMainWindow, ui.Ui_DataToSim):
    def __init__(self, parent=None):
        super(DataToSim, self).__init__(parent)
        self.setupUi(self)

    def selectfile(self):
        self.pathLineEdit.setText(QtGui.QFileDialog.getOpenFileName(self, 'Open data file', os.getenv("HOME"),
                                                                    "Text files (*.txt *.data *.sav *.log)"))
        if self.pathLineEdit.text() == "":
            self.nextButton.setDisabled(True)
        else:
            self.nextButton.setEnabled(True)

    def main(self):
        self.nextButton.setDisabled(True)
        self.browseButton.clicked.connect(self.selectfile)
        self.closeButton.clicked.connect(app.exit)
        self.show()


if __name__ == '__main__':
    app = QtGui.QApplication(sys.argv)
    appView = DataToSim()
    appView.main()
    app.exec_()
