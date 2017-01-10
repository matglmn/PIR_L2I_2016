# PIR_L2I_2016
Architecture for flight data acquisition and replay on simulator

1) FlightData Acquisition
Java-based Android app for flight parameters acquisition

Parameters acquired :
- location (latitude, longitude, altitude)
- speed
- roll, yaw and pitch angles

Start the acquisition with the start button, set a marker when you want to save data and stop acquisition with the stop button.
Data is saved in .json format on external storage when the acquisition is stopped.
(Data file name format : MMDDYYYY_HHMMSS_data.txt)

2) Data to Sim
Python-based interface used to import a flight data file (.json) and replay it on Flightgear simulator. 
LINUX ONLY AT THE MOMENT.

- Import the desired data file with the data browser
- Select the marker you want to replay
- Modify the flight parameters if necessary and launch simulation by click on 'Finish' button

Flightgear simulator is launched with selected data and the simulation starts.

For any question concerning this project, contact matthieu.gaulmin@alumni.enac.fr



