package com.supgro.Controller.Model

class TextPost {

    private var postid: String = ""
    private var posttext: String = ""
    private var publisher: String = ""

    constructor()
    constructor(postid: String, posttext: String, publisher: String) {
        this.postid = postid
        this.posttext = posttext
        this.publisher = publisher

    }

    //___________________________________________

    fun getPostId(): String{
        return postid
    }
    fun setPostId(postid: String){
        this.postid = postid
    }
//___________________________________________

    fun getPostText(): String{
        return posttext
    }
    fun setPostText(posttext: String){
        this.posttext = posttext
    }
//___________________________________________

    fun getPublisher(): String{
        return publisher
    }
    fun setPublisher(publisher: String){
        this.publisher = publisher
    }
//___________________________________________

}