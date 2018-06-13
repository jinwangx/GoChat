package com.jw.gochat.utils

import com.jw.business.model.bean.NetTask
import java.util.HashMap

object BackTaskFactory {

    fun newFriendAcceptTask(invitor: String, acceptor: String): NetTask {
        val task = NetTask()
        task.method = "POST"
        val params = HashMap<String, String>()
        params["invitor"] = invitor
        params["acceptor"] = acceptor
        task.params = params
        task.path = "/friend/accept"
        task.protocol = "HTTP"
        return task
    }

    fun userIconChangeTask(name: String, iconPath: String): NetTask {
        val task = NetTask()
        task.method = "POST"
        task.type = NetTask.TYPE_UPLOAD
        val files = HashMap<String, String>()
        files["$name.png"] = iconPath
        val params = HashMap<String, String>()
        task.params = params
        task.files = files
        task.path = "/user/invitator_icon"
        task.protocol = "HTTP"
        return task
    }

    fun userNameChangeTask(name: String): NetTask {
        val task = NetTask()
        task.method = "POST"
        val params = HashMap<String, String>()
        params["invitator_name"] = name
        task.params = params
        task.path = "/user/invitator_name"
        task.protocol = "HTTP"
        return task
    }
}