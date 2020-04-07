Overview
----
This repository is a PyTorch implementation of the paper "COVID-Xpert: An AI Powered Population Screening of COVID-19 Cases Using Chest Radiography Images".

Dataset
-----
* Pre-trained Dataset
  * [ChestX-ray8: Hospital-scale Chest X-ray Database and Benchmarks on Weakly-Supervised Classification and Localization of Common Thorax Diseases.](https://nihcc.app.box.com/v/ChestXray-NIHCC)  
* [COVID-19 Image Data Collection.](https://github.com/ieee8023/covid-chestxray-dataset)  
* [RSNA Pneumonia Detection Challenge.](https://www.kaggle.com/c/rsna-pneumonia-detection-challenge)  


COVID-Xpert Architecture
----
![](readme/transfer_learning.PNG)

Some Results
----
Longitudinal XCR images of a patient over the four time points.  

![](readme/one_patient.PNG)



Models
----
The model weights are available [here](https://drive.google.com/drive/folders/1AUtsxjPNVJiTboFFTzzqyeCKBPvMxbII?usp=sharing).  
You can test your own image by:
```
python testing.py --image <X-ray path>
```

Thumbnail of Generated Heatmaps
-----
![](readme/heatmap.PNG)




Dependencies
-----
* Python 3.7
* Pytorch 0.4.0


