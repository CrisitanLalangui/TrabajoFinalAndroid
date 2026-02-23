from django.contrib.messages import success
from rest_framework import status
from rest_framework.parsers import MultiPartParser, FormParser
from rest_framework.permissions import AllowAny
from rest_framework.response import Response
from rest_framework.views import APIView

from usuariosStudyBro.models import ArchivoModel
from usuariosStudyBro.serializers import ligarArchivoSerializer


class consultarArchivosStudyView(APIView):
    permission_classes = (AllowAny,)

    def get(self, request):
        queryEmail = request.GET.get('email')

        if  queryEmail:
            archivos = ArchivoModel.objects.filter(usuario__email=queryEmail)


        else:

            archivos = ArchivoModel.objects.all()
        data = [
            {
                "slugArchivo": archivo.slug,
                "archivo": {
                    "name": archivo.name,
                    "email": archivo.usuario.email if archivo.usuario else "",

                    "nombreTarjeta": archivo.nombreTarjeta,
                    "url": f"{archivo.url}"
                }
            }

            for archivo in archivos
        ]

        return Response(
            {"success": True, "data": data}, status=status.HTTP_200_OK)

class ligarArchivoStudyView(APIView):
    permission_classes = [AllowAny]
    parser_classes = (MultiPartParser, FormParser)

    def post(self, request):
        serializer = ligarArchivoSerializer(data=request.data)

        if serializer.is_valid():
            archivo = serializer.save()

            data = {
                "name": archivo.name,
                "email": archivo.usuario.email if archivo.usuario else "",
                "nombreTarjeta": archivo.nombreTarjeta,
                "url": archivo.url.url
            }
            return Response({"success": True, "data": data}, status=status.HTTP_200_OK)

        # errores de validación
        errores = []
        for error_list in serializer.errors.values():
            errores.extend(error_list)

        return Response({"success": False, "errores": errores}, status=status.HTTP_400_BAD_REQUEST)
