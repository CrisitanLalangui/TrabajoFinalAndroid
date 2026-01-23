solo se teoca esta carpeta cuando tocasmos los archivos, dentro de este





#Tipos de relaciones
Es obliarorio que pongamos el null= True y blank = True en los elementos que puecdan quedar vacíos.
(Ya sabemos que por defecto están como True, pero django lo exige para no tencer errores.)

##1:1 -> models.OneToOneField
Se debe de guardar primero el objeto en la base de datos que se quiere relacionar. Luego se vincula ese objeto guardado con el otro elemento que vamos a guardar. Es importante porque si no no deja guardar el conjunto completo.

##1:N -> models.ForeignKey
En este tipo de relación se puede ir creando los objetos y vinculándolos a la vez. Finalmente, se procede a hacer el save() -> Método usado para guardar en la base de datos.

##N:M -> models.ManyToManyField
Se debe de guardar primero el objeto en la base de datos que se quiere relacionar. Luego se vincula ese objeto guardado con el otro elemento que vamos a guardar. Es importante porque si no no deja guardar el conjunto completo.

### ON_DELETE

### models.SET_NULL

Esto permite que si se borra un elemento ligado a este objeto no sea alterado si se actualiza/borra el objeto ligado a él.

###