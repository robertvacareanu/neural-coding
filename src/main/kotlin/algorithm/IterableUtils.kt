package algorithm

/**
 * Created by robert on 4/19/18.
 * Some iterable utils for handling multi lambda parameters applied sequentially on a list
 */
fun <T> Iterable<T>.multiMap(transforms: List<(T) -> T>): List<T> = if (transforms.isNotEmpty()) {
    this.map {
        transforms.drop(1).fold(transforms.first()(it)) { acc, function ->
            function(acc)
        }
    }
} else this.toList()

fun <T> Iterable<T>.multiMapRight(transforms: List<(T) -> T>): List<T> = if (transforms.isNotEmpty()) {
    this.map {
        transforms.dropLast(1).foldRight(transforms.last()(it)) { function, acc ->
            function(acc)
        }
    }
} else this.toList()

fun <T> Iterable<T>.multiFilter(filters: List<(T) -> Boolean>): List<T> = if (filters.isNotEmpty()) {
    this.filter {
        filters.fold(true) { acc, function ->
            acc and function(it)
        }
    }
} else this.toList()