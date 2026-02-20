from django.db import models
from rest_framework import serializers
from usuariosStudyBro.models import ArchivoModel
from usuariosStudyBro.models import UserStudy


class ligarArchivoSerializer(serializers.ModelSerializer):
    name = serializers.CharField(required=True)
    email = serializers.EmailField(required=True)
    url = serializers.FileField(required=True)
    idTarjeta = serializers.CharField(required=True)

    class Meta:
        model = ArchivoModel

        fields = (
            "name", "email", "url", "idTarjeta"
        )

    # Estas clases se validan solas, porque el validate_(nombre) lleva el nombre del serializer
    def validate_name(self, value):
        exist = ArchivoModel.objects.filter(name=value).exists()
        if exist:
            raise serializers.ValidationError("Ya hay un archivo con un mismo nombre  existe")

        return value

    def validate_url(self, value):
        if value is None:
            raise serializers.ValidationError("Debes adjuntar un archivo")
        return value

    def validate_email(self, value):
        exist = UserStudy.objects.filter(email=value).exists()
        if not exist:
            raise serializers.ValidationError("El correo asociado no existe")

        if any(domain in value for domain in [".ru", ".xyz"]):
            raise serializers.ValidationError("El dominio del correo no está permitido")

        return value

    def create(self, validated_data):

        emailUsuario = validated_data["email"]

        # 2. Buscamos la instancia del usuario (ya sabemos que existe por tu validate_email)
        emailUser = UserStudy.objects.get(email=emailUsuario)

        archivo = ArchivoModel.objects.create(

            name=validated_data["name"],
            url=validated_data["url"],
            idTarjeta=validated_data["idTarjeta"],
            usuario = emailUser

        )

        return archivo
