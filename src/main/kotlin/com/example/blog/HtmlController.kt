package com.example.blog

import ArticleRepository
import UserRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@Controller
class HtmlController(private val repository: ArticleRepository,
                     private val properties: BlogProperties) {

    @GetMapping("/")
    fun blog(model: Model): String {
        model["title"] = properties.title
        model["banner"] = properties.banner
        model["articles"] = repository.findAllByOrderByAddedAtDesc().map { it.render() }
        return "blog"
    }
    @RestController
    @RequestMapping("/api/article")
    class ArticleController(private val repository: ArticleRepository) {

        @GetMapping("/")
        fun findAll() = repository.findAllByOrderByAddedAtDesc()

        @GetMapping("/{slug}")
        fun findOne(@PathVariable slug: String) =
            repository.findBySlug(slug) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "This article does not exist")

    }

    @RestController
    @RequestMapping("/api/user")
    class UserController(private val repository: UserRepository) {

        @GetMapping("/")
        fun findAll() = repository.findAll()

        @GetMapping("/{login}")
        fun findOne(@PathVariable login: String) =
            repository.findByLogin(login) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "This user does not exist")
    }



}

