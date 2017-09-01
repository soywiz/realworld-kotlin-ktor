package me.avo.realworld.kotlin.ktor.server

import org.jetbrains.ktor.routing.*

fun Routing.setup() {

    route("api") {

        route("users") {

            post("login") {
                TODO("Login")
            }

            post {
                TODO("Registration")
            }
        }

        route("user") {

            get {
                TODO("Get User")
            }

            put {
                TODO("Update User")
            }
        }

        route("profiles") {

            route("{username}") {

                get {
                    TODO("Get Profile")
                }

                post("follow") {
                    TODO("Follow User")
                }

                delete("follow") {
                    TODO("Unfollow User")
                }

            }

        }

        route("articles") {

            get {
                TODO("Get articles")
            }

            get("feed") {
                TODO("Feed Articles")
            }


            post {
                TODO("Create Article")
            }

            route("{slug}") {

                get {
                    TODO("Get Article")
                }

                put {
                    TODO("Update Article")
                }

                delete {
                    TODO("Delete Article")
                }

                route("comments") {
                    post {
                        TODO("Add Comments to an Article")
                    }

                    get {
                        TODO("Get Comments from an Article")
                    }

                    delete("{id}") {
                        TODO("Delete Comment")
                    }

                }

                route("favorite") {
                    post {
                        TODO("Favorite Article")
                    }

                    delete {
                        TODO("Unfavorite Article")
                    }
                }

            }


        }

        get("tags") {
            TODO("Get Tags")
        }
    }
}