from rest_framework import status
from rest_framework.response import Response
from rest_framework.views import APIView
from rest_framework.permissions import IsAuthenticated

from Productos.models import Producto
from Productos.serializers import ProductoSerializer


class ProductoView(APIView):
    permission_classes = [IsAuthenticated]  # solo si está autenticado

    def get(self, request):


        productos = Producto.objects.all()

        data = ProductoSerializer(productos,many=True).data  # Se crea data porque se está creando un datoy hay que guardarlo en un bdd

        """   data = [
               {
                    "id" : producto.id,
                   "nombre" : producto.nombre,
                   "precio":producto.precio,
                   "slug": producto.slug,
                   "categoria" : producto.categoria.nombre
               } 
               for producto in productos
           ]"""

        return Response({"data": data}, status=status.HTTP_200_OK)

