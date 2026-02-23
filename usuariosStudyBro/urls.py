from django.urls import path

from usuariosStudyBro.views import *

urlpatterns = [
    path("consultarArchivosStudyBro/", consultarArchivosStudyView.as_view()),  # http://localhost:8000/api/registro/
    path("usuariosStudyBro/", RegisterViewStudyBro.as_view()),
    path("ligarArchivosStudyBro/", ligarArchivoStudyView.as_view()),
    path("loginStudyBro/", LoginViewStudyBro.as_view()),
    path("registroStudyBro/", RegisterViewStudyBro.as_view()),


]
