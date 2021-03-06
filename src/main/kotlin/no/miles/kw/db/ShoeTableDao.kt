package no.miles.kw.db


import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.transactions.transaction
import javax.sql.DataSource

class ShoeTableDao(dataSource: DataSource) {

    private val database = Database.connect(dataSource)
    private val table = ShoeTable

    fun insert(shoe: Shoe): InsertStatement<Number> =
        transaction(database) {
            table.insert {
                it[table.name] = shoe.name
                it[table.brand] = shoe.brand
            }.also { println("Inserted shoe. (name: ${shoe.name}, type: ${shoe.brand} )") }
    }

    fun get(): List<Shoe> =
        transaction(database) {
            ShoeTable.selectAll().map { it.toShoe() }
        }

    fun createTable() {
        transaction {
            SchemaUtils.create(ShoeTable)
        }
    }
}

private fun ResultRow.toShoe() =
    Shoe(this[ShoeTable.name], this[ShoeTable.brand])