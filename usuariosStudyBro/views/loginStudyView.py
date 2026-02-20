from rest_framework import status
from rest_framework.permissions import AllowAny
from rest_framework.response import Response
from  rest_framework.views import APIView

from usuariosStudyBro.serializers import LoginSerializer


#Esta clase tiene que heredar de su padre que comunica los datos del front con el bakceend,


class LoginViewStudyBro(APIView):

    permission_classes = [AllowAny]

    def post(self,request):#se usa un método post, para enviar información, y porque la contraseña va encriptada.
        datos = request.data #caputra lo que el usuario escribió mediante (request.data)
        serializer = LoginSerializer(data=datos) #invoca a LoginSerializer


        if  serializer.is_valid():  #si Seriazlier no ha arrojado ningún error en las validaciones -> responde con un OK 200
            return Response(serializer.validated_data, status= status.HTTP_200_OK)

        errores = [] # Si serialier  arrojó un error responde con un OK 400.
                     # almacenamos todos los errores en un array

        # Un bucle, queue añadira todos los errores que devuelva serializer

        for error in serializer.errors.values(): #Bucle anidado
            for e in error:
                errores.append(e)


                ### Envio de respuesta al exterior en formato JSON ###

        return Response({"succcess":False, "errores":errores}, status=status.HTTP_400_BAD_REQUEST) #Envia una resupesta en formato JsIN



