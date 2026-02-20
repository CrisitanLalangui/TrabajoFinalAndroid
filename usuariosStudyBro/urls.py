from django.urls import path

from usuariosStudyBro.views.registerStudyView import RegisterViewStudyBro, RegisterStudyBro
from usuariosStudyBro.views.loginStudyView import LoginViewStudyBro
from usuariosStudyBro.views.ligarArchivoStudyView import ligarArchivoStudyView, consultarArchivosStudyView

urlpatterns = [

    path("registroStudyBro/",RegisterStudyBro .as_view()),  # http://localhost:8000/api/registro/
    path("usuariosStudyBro/", RegisterViewStudyBro.as_view()),
    path("loginStudyBro/", LoginViewStudyBro.as_view()),
    path("consultarArchivosStudyBro/", consultarArchivosStudyView.as_view()),
    path("ligarArchivosStudyBro/", ligarArchivoStudyView.as_view()),



]