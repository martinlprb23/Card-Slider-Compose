package com.roblescode.slider.utils

fun getRandomImage():String{
    return "https://picsum.photos/800?random="+(0..999).random()
}