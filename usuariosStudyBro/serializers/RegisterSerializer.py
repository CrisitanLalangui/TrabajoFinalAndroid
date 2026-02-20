from rest_framework import serializers
from usuariosStudyBro.models.userStudyModel import UserStudy


class RegisterSerializer(serializers.ModelSerializer):
    email = serializers.EmailField(required=True)
    password1 = serializers.CharField(required=True, min_length=6)  # Contraseña principal
    password2 = serializers.CharField(required=True, min_length=6)  # Confirmación

    class Meta:

        model = UserStudy

        fields = (
            "email",
            "password1", "password2",


        )

    def validate_email(self, value):

        exist = UserStudy.objects.filter(email=value).exists()

        if exist:
            raise serializers.ValidationError("El correo ya existe")

        if any(domain in value for domain in [".ru", ".xyz"]):
            raise serializers.ValidationError("El dominio del correo no está permitido")

        return value

    def validate_password1(self, value):
        # Obligamos a que la contraseña tenga al menos un número por seguridad
        tiene_numero = any(letra.isdigit() for letra in value)
        if not tiene_numero:
            raise serializers.ValidationError("Mínimo un carácter numérico")
        return value

    # --- 3. VALIDACIÓN GENERAL (DICCIONARIO ATTRS) ---

    def validate(self, attrs):
        # Aquí comparamos campos entre sí usando el diccionario 'attrs'
        # Verificamos que el usuario escribió lo mismo en ambas cajas de contraseña
        if attrs["password1"] != attrs["password2"]:
            raise serializers.ValidationError("Contraseñas no coinciden")
        return attrs  # Devolvemos todos los datos listos para el siguiente paso

    # --- 4. CREACIÓN FINAL (GUARDADO EN BD) ---

    def create(self, validated_data):

        validated_data.pop("password2")

        password = validated_data.pop("password1")

        user = UserStudy.objects.create(
            email=validated_data["email"]

        )

        user.set_password(password)

        user.save()

        return user


