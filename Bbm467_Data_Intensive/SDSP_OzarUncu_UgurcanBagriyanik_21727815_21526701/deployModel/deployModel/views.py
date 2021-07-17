from deployModel import forms
from django.http import HttpResponse, request
from django.http import HttpResponseRedirect
from django.shortcuts import render
import joblib
from .forms import CreateNewList
import sklearn

def home(request):
    cls = joblib.load("LR.sav")
    print(cls.get("features"))
    return render(request,"home.html",{"features":cls.get("features")})


def analyze(request):
    if request.method == "POST":
        form = CreateNewList(request.POST)
        if form.is_valid():
            cls = joblib.load("LR.sav")
            lis = []
            for i in cls.get("features"):
                lis.append(request.POST[i])
            model = cls.get('model')
            prob = model.predict_proba([lis])
            x = zip(model.classes_,prob[0])

            return render(request,"result.html",{"dict":x})

        return HttpResponseRedirect("result")
    else:
        form = CreateNewList()
    return render(request, "home.html", {"form":form})


