package barrios.abrahan.misnotas

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream
import java.util.jar.Manifest

class AgregarNotaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_nota)

        var btn_guardar: Button = findViewById(R.id.btn_guardar)

        btn_guardar.setOnClickListener {
            guardar_nota()
        }

    }

    fun guardar_nota(){

        //Verifica que contenga los permisos
        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Si no los tiene, los pide al usuario
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                235
            )
            //Si tiene permisos, procede a guardar
        }else{
            guardar()

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode){
            235 -> {
                //Pregunta si el usuario acepto los permisos
                if((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                    guardar()
                }else{
                    //Si no acepto, coloca un mensaje
                    Toast.makeText(this, "Error: Permisos denegados", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    public fun guardar(){

        var et_titulo: EditText = findViewById(R.id.et_titulo)
        var titulo = et_titulo.text.toString()

        var et_contenido: EditText = findViewById(R.id.et_contenido)
        var contenido = et_contenido.text.toString()

        if (titulo==""||contenido==""){
            Toast.makeText(this, "Error: Campos Vacios",Toast.LENGTH_SHORT).show()
        }else{
            try{
                val archivo = File(ubicacion(),titulo+".txt")
                val fos = FileOutputStream(archivo)
                fos.write(contenido.toByteArray())
                fos.close()
                Toast.makeText(
                    this,
                    "Se guardó el archivo en la carpeta pública",
                    Toast.LENGTH_SHORT
                ).show()
            }catch (e: Exception){
                Toast.makeText(this, "Error: no se guardó el archivo",Toast.LENGTH_SHORT).show()
            }
        }

        finish()
    }

    private fun ubicacion():String{

        val carpeta = File(getExternalFilesDir(null),"notas")

        if(!carpeta.exists()){
            carpeta.mkdir()
        }

        return carpeta.absolutePath
    }
}