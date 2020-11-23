# PARTE TEORICA

### Lifecycle

#### Explica el ciclo de vida de una Activity.

##### ¿Por qué vinculamos las tareas de red a los componentes UI de la aplicación?
Simplemente porque no es posible mostrar la información en caso de que los componentes UI no existan. En este caso es necesario que se asegure de que los 
componentes UI sean creados antes para evitar problemas, o en su defecto, cancelar manualmente el proceso para evitar crash en la aplicación.

##### ¿Qué pasaría si intentamos actualizar la recyclerview con nuevos streams después de que el usuario haya cerrado la aplicación?
La tarea se cancela automáticamente ya que la recyclerview ya no existe. Por ejemplo, en el caso de rotación de la pantalla, si se rota antes de terminar la operación de red, al intentar hacer webView.visibility = View.GONE por ejemplo, se estaría actualizando una vista que ya no existe.

##### Describe brevemente los principales estados del ciclo de vida de una Activity.

onCreate(Bundle): Se llama en la creación de la actividad. Se utiliza para realizar todo tipo de inicializaciones, como la creación de la interfaz de usuario o la inicialización de estructuras de datos. Puede recibir información de estado dela actividad (en una instancia de la clase Bundle), por si se reanuda desde una actividad que ha sido destruida y vuelta a crear.

onStart(): Nos indica que la actividad está a punto de ser mostrada al usuario.

onResume(): Se llama cuando la actividad va a comenzar a interactuar con el usuario. Es un buen lugar para lanzar las animaciones y la música.

onPause(): Indica que la actividad está a punto de ser lanzada a segundo plano, normalmente porque otra actividad es lanzada. Es el lugar adecuado para detener animaciones, música o almacenar los datos que estaban en edición.

onStop(): La actividad ya no va a ser visible para el usuario. !Ojo si hay muy poca memoria! es posible que la actividad se destruya sin llamar a este método.

onRestart(): Indica que la actividad va a volver a ser representada después de haber pasado por onStop().

onDestroy(): Se llama antes de que la actividad sea totalmente destruida. Por ejemplo, cuando el usuario pulsa el botón de volver o cuando se llama al método finish(). Ojo si hay muy poca memoria, es posible que la actividad se destruya sin llamar a este método.

---

### Paginación 

#### Explica el uso de paginación en la API de Twitch.

##### ¿Qué ventajas ofrece la paginación a la aplicación?
Entre las principales ventajas que se tiene con la paginación están: 
- Reducción de recursos de la aplicación. Al aplicación toma menos tiempo en usar recursos para mostrar datos
- Aumento de velocidad de carga y repuesta. Mejora la experiencia de usuario al mostrar datos de forma rápida, y no quedarse bloqueado trayendo toda la información

##### ¿Qué problemas puede tener la aplicación si no se utiliza paginación?
En cuanto a experiencia de usuario, éste al ver que la aplicación tarda bastante en mostrar la información, simplemente la dejará de usar. El otro problema es la lentitud con la que el aplicativo mostraría la información, consumiendo recursos, y decepcionando al usuario.

##### Lista algunos ejemplos de aplicaciones que usan paginación.
Entre las más comunes y debido al nivel de datos a mostrar tenemos:
- Facebook
- Whatsapp
- Twitter
- Instagram
