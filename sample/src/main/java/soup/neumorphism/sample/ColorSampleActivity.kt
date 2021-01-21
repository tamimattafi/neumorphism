package soup.neumorphism.sample

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import soup.neumorphism.sample.databinding.ActivitySampleColorBinding

class ColorSampleActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySampleColorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySampleColorBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
    }

}
