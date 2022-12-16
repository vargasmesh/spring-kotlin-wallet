package local.app
import local.app.application.command.AccountService
import local.app.domain.model.CreateAccountEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@SpringBootApplication
class AppApplication

fun main(args: Array<String>) {
	runApplication<AppApplication>(*args)
}


@Controller
class HelloController(
	@Autowired
	val accountService: AccountService
) {

	@QueryMapping
	fun hello(): String {
		accountService.createAccount(CreateAccountEvent("213c263e-5c6c-458e-a854-264e24b3ce46", "test"))
		return "World"
	}
}