package com.example.android.asteroidradar

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.asteroidradar.main.AsteroidsAdapter
import com.example.android.asteroidradar.models.Asteroid
import com.squareup.picasso.Picasso


@BindingAdapter("PicOfDayContentDescription")
fun PicOfDayDescription(imageView: ImageView, title: String?) {
    val context = imageView.context
    if (title==null) {
        imageView.contentDescription=context.getString(R.string.this_is_nasa_s_picture_of_day_showing_nothing_yet)
    } else {
        imageView.contentDescription= String.format(context.getString(R.string.nasa_picture_of_day_content_description_format), title)
    }
}
@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
    }
}


@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
    }
}


@BindingAdapter("statusImageContentDescription")
fun statusImageContentDescription(imageView: ImageView, isHazardous: Boolean) {
    val context = imageView.context
    if (isHazardous) {
        imageView.contentDescription=context.getString(R.string.potentially_hazardous_asteroid_image)
    } else {
        imageView.contentDescription= context.getString(R.string.not_hazardous_asteroid_image)
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}


@BindingAdapter("imageUrl")
fun setImageUrl(imageView: ImageView, url:String?) {
    if (url != null)
        Picasso.get().load(url).into(imageView)
}


@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Asteroid>?) {
    val adapter = recyclerView.adapter as AsteroidsAdapter
    adapter.submitList(data)
}

@BindingAdapter("goneIfNotNull")
fun goneIfNotNull(view: View, it: Any?) {
    view.visibility = if (it != null) View.GONE else View.VISIBLE
}