package local.app.adapter.db

import jakarta.persistence.*
import com.github.ksuid.Ksuid


@Entity
@Table(name = "events")
class Event(
    @Id
    val id: String = Ksuid.newKsuid().toString(),
    val type: String,
    val entity_id: String,
    val data: String? = null,
)

@Entity
@Table(name = "accounts")
class Account(
    @Id
    var id: String,
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "last_summary_event", referencedColumnName = "id")
    var last_summary_event: Event,
) {
}