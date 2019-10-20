#!/bin/bash

sudo gnome-terminal --title="KWIC - Orchestration Service" -- sh -c "cd kwic-orchestration-service; sh build-with-docker.sh; bash";
sudo gnome-terminal --title="KWIC - Input Service" -- sh -c "cd chairwise-input-service; sh build-with-docker.sh; bash";
sudo gnome-terminal --title="KWIC - Circular Shift Service" -- sh -c "cd chairwise-circular-shift-service; sh build-with-docker.sh; bash";
sudo gnome-terminal --title="KWIC - Sorting Service" -- sh -c "cd chairwise-sorting-service; sh build-with-docker.sh; bash";
sudo gnome-terminal --title="KWIC - Output Service" -- sh -c "cd chairwise-output-service; sh build-with-docker.sh; bash";
