package com.supgro.Controller.Model

class Notification (var userid: String = "", var text: String = "", var postid: String = "",var notification: String, var ispost: Boolean = false  ) {

    constructor() : this("", "", "","", false )

}