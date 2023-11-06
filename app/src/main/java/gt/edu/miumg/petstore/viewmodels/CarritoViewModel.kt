package gt.edu.miumg.petstore.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import gt.edu.miumg.petstore.models.CarritoState

class CarritoViewModel: ViewModel() {
    var _carrito by mutableStateOf(mutableListOf<CarritoState>())
        private set

    var showBottomSheet by mutableStateOf(false)

    fun toggleCarritoItem(carritoState: CarritoState) {
        if (_carrito.contains(carritoState)) {
            removeCarrito(carritoState)
        } else {
            addCarrito(carritoState)
        }
    }

    fun addCarrito(carritoState: CarritoState) {
        _carrito.add(carritoState)
    }

    fun removeCarrito(carritoState: CarritoState) {
        _carrito.remove(carritoState)
    }

    fun clearCarrito() {
        _carrito.clear()
    }

    fun totalCarrito(): Double {
        var total = 0.0
        _carrito.forEach {
            total += it.price * it.quantity
        }
        return total
    }

    fun totalCarritoItems(): Int {
        var total = 0
        _carrito.forEach {
            total += it.quantity
        }
        return total
    }

    fun totalCarritoItemsByUid(uid: String): Int {
        var total = 0
        _carrito.forEach {
            if (it.uid == uid) {
                total += it.quantity
            }
        }
        return total
    }

    fun incrementCarritoItem(carritoState: CarritoState) {
        _carrito.forEach {
            if (it.uid == carritoState.uid) {
                it.quantity += 1
            }
        }
    }

    fun decrementCarritoItem(carritoState: CarritoState) {
        _carrito.forEach {
            if (it.uid == carritoState.uid) {
                it.quantity -= 1
            }
        }
    }

    fun removeCarritoItem(carritoState: CarritoState) {
        _carrito.remove(carritoState)
    }

    fun getCarrito(): List<CarritoState> {
        return _carrito
    }
}