

from rest_framework import status
from rest_framework.permissions import AllowAny
from rest_framework.response import Response
from rest_framework.views import APIView

from usuariosStudyBro.models import UserStudy
from usuariosStudyBro.serializers import RegisterSerializer


class RegisterViewStudyBro(APIView):
    permission_classes = (AllowAny,)


    def get(self, request):

        usuarios = UserStudy.objects.all()  # Con esta consulta tenemos un array de objetos de tipo usuario


        usuarios2 = UserStudy.objects.all().order_by("-email")

        data2 = [{"email": usuario.email} for usuario in usuarios]



        return Response(
            {"success": True, "data": data2}, status=status.HTTP_200_OK)


    def post(self, request):

        # Si queremos acceder a los elementos del body, usamos request.data
        # request.data = {'email': 'pepe@gmail.com', 'username': 'pepe_97', 'first_name': 'Pepe', 'last_name': 'Perez'}
        data = request.data
        serializer = RegisterSerializer(data=data)
        if serializer.is_valid(): # si todas las validaciones del serializer son válidas
            serializer.save() # guardamos los datos que ha envidado ->
            # se llama al metodo create que crea los datos en el serializer
            return Response({"success": True}, status=status.HTTP_200_OK)
        else:

            # {"email": ["El correo ya existe"], "username": ["El nombre de usuario ya existe"]}

            errores = []            #Si el serializer lanza errores, se recorre el array de errores, e imprimiremos el array de errores
            for error in serializer.errors.values():
                for e in error:
                    errores.append(e)

            return Response({"success": False, "errors": errores}, status=status.HTTP_400_BAD_REQUEST)
