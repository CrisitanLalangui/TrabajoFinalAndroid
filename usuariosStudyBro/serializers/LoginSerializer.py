from rest_framework import serializers
from rest_framework_simplejwt.tokens import RefreshToken

from usuariosStudyBro.models import UserStudy


class LoginSerializer(serializers.ModelSerializer):
    email = serializers.EmailField(required=True)
    password = serializers.CharField(required=True, write_only=True, min_length=6)

    class Meta:  # La tabla para personalizar nuestra configuracion
        model = UserStudy  # Aquñi elegimos con que datos vamos a trabjar
        fields = ('email', 'password')

    def validate(self, attrs):  # nos llega todos los datos de flront a trvés del atrs, (didcionario)
        # Validar correo electrónico

        if '@' not in attrs['email']:
            raise serializers.ValidationError(
                "Correo electrónico no tiene @")  # Se arroja un error de tipo "serializers"

        if any(domain in attrs["email"] for domain in [".ru", ".xyz"]):
            raise serializers.ValidationError("El dominoi del correo no está permitido")
        # Validar contraseña

        tienenumero = any(letra.isdigit() for letra in attrs["password"])

        if not tienenumero:
            raise serializers.ValidationError("Mínimo un carácter numérico")

        # Si el usuario pasa todas las
        # validaciones le dejamos iniciar sesión

        # Buscamos el correo (Si usuario existe)
        # SELECT * FROM User WHERE  email = attrs["email"]

        # Nos retorna el primer objeto de la query o un None
        user = UserStudy.objects.filter(email=attrs["email"]).first()



        # Aquí comprobamos que el usuario exisste, en caso contrario arrojamos un error de tipo Serializers
        if not user: # i user is no es note
            raise serializers.ValidationError("el email no existe")

        if not user.check_password(attrs["password"]):
            raise serializers.ValidationError("Usuario o ocntraseña incorrectos")



        # INiciamos sesión
        refresh = RefreshToken.for_user(user) # Diccionario


        return {

            "success" : True,
            "data" : {
                "refreshToken" : str(refresh),
                "token" : str(refresh.access_token),
                "email" : user.email

            }
        }


