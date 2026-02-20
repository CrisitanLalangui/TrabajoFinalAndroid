from rest_framework import serializers
from Users.models import User


class RegisterSerializer(serializers.ModelSerializer):
    # --- 1. DEFINICIÓN DE CAMPOS (EL CONTRATO) ---
    # Aquí definimos qué datos esperamos del front y qué reglas deben cumplir
    email = serializers.EmailField(required=True)
    username = serializers.CharField(required=True)
    first_name = serializers.CharField(required=True, min_length=3)
    last_name = serializers.CharField(required=True, min_length=3)
    password1 = serializers.CharField(required=True, min_length=6)  # Contraseña principal
    password2 = serializers.CharField(required=True, min_length=6)  # Confirmación

    class Meta:
        # Vinculamos este serializer con el modelo User de la base de datos
        model = User
        # Lista de campos que el serializer va a reconocer y procesar
        fields = (
            "email", "username", "first_name",
            "last_name", "password1", "password2"
        )

    # --- 2. VALIDACIONES ESPECÍFICAS (INDIVIDUALES) ---

    def validate_email(self, value):
        # Buscamos en la BD si el correo ya existe para no tener duplicados
        exist = User.objects.filter(email=value).exists()
        if exist:
            raise serializers.ValidationError("El correo ya existe")

        # Filtramos correos de países o dominios que no queremos permitir
        if any(domain in value for domain in [".ru", ".xyz"]):
            raise serializers.ValidationError("El dominio del correo no está permitido")

        return value  # Si todo está bien, devolvemos el valor limpio

    def validate_username(self, value):
        # Verificamos que el nombre de usuario sea único en nuestra base de datos
        if User.objects.filter(username=value).exists():
            raise serializers.ValidationError("El nombre de usuario ya existe")
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
        # Limpieza: Borramos la confirmación (password2) porque no se guarda en la BD
        validated_data.pop("password2")

        # Extracción: Sacamos la contraseña del diccionario para tratarla con seguridad
        password = validated_data.pop("password1")

        # Creamos el registro del usuario con los datos restantes (email, nombre, etc.)
        user = User.objects.create(
            email=validated_data["email"],
            username=validated_data["username"],
            first_name=validated_data["first_name"],
            last_name=validated_data["last_name"],
        )

        # ENCRIPTACIÓN: Convertimos la contraseña en un código secreto ilegible
        user.set_password(password)

        # Guardamos definitivamente los cambios en la base de datos
        user.save()

        # Retornamos el objeto usuario recién creado
        return user