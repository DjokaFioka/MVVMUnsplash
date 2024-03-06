package rs.djokafioka.mvvmunsplash.ui.details

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import rs.djokafioka.mvvmunsplash.R
import rs.djokafioka.mvvmunsplash.databinding.FragmentDetailsBinding

/**
 * Created by Djordje on 26.2.2022..
 */
class DetailsFragment : Fragment(R.layout.fragment_details) {
    private val args by navArgs<DetailsFragmentArgs>() //Preuzimamo ono sto je prosledjeno fragmentu preko navigation component

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentDetailsBinding.bind(view)

        binding.apply {
            val photo = args.photo //photo - tako smo nazvali u nav graph arguments za ovaj fragment

            Glide.with(this@DetailsFragment)
                .load(photo.urls.regular) //Zameniti sa regular, bilo je full samo za potrebe testiranja
                .error(R.drawable.ic_error)
                .listener(object: RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.isVisible = false
                        return false //mora false inace necemo videti sliku
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.isVisible = false
                        textViewCreator.isVisible = true
                        textViewDescription.isVisible = photo.description != null
                        return false
                    }
                })
                .into(imageView)

            textViewDescription.text = photo.description

            val uri = Uri.parse(photo.user.attributionUrl)
            val intent = Intent(Intent.ACTION_VIEW, uri)

            textViewCreator.apply {
                text = "Photo by ${photo.user.name} on Unsplash"
                setOnClickListener {
                    context.startActivity(intent) //implicit intent
                }
                paint.isUnderlineText = true
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Opa opa"
    }
}