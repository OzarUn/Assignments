from django import forms
from django.forms.forms import Form
import joblib


yes_no =(
    (1,"yes"),
    (2,"no")
    )
women_man =(
(1,"man"),
(2,"women")
)
morning_type = (
    (1 , "1-2 Days a Month"),
    (2 , "3-4 Days a Week"),
    (3 , "1-2 Days a Week"),
    (4, "Every Day")
)
day_time = (
    (0,"Evenings"),
    (1,"No Difference"),
    (2,"Mornings")
)


arr_1_for_floats = ["Feature_2","Feature_3","Feature_4","Feature_5","Feature_47","Feature_48","Feature_49","Feature_50"]


class CreateNewList(forms.Form):
    def __init__(self,*args,**kwargs):
        super(CreateNewList,self).__init__(*args,**kwargs)
        cls = joblib.load("LR.sav")
        for i in cls.get("features"):
            if i == "Feature_1":
                self.fields[i] = forms.ChoiceField(choices=women_man)
            elif i in arr_1_for_floats:
                self.fields[i] = forms.CharField(max_length=100)
            elif i == "Feature_29":
                self.fields[i] = forms.ChoiceField(choices=morning_type)
            else :
                self.fields[i] = forms.ChoiceField(choices=yes_no)


        

