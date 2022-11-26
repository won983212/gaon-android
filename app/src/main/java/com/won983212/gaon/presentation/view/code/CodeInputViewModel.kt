package com.won983212.gaon.presentation.view.code

import com.won983212.gaon.presentation.base.BaseViewModel
import com.won983212.gaon.presentation.util.SingleLiveEvent
import com.won983212.gaon.presentation.util.asLiveData

class CodeInputViewModel : BaseViewModel(), PasswordInputListener {

    private val _eventSuccess = SingleLiveEvent<Unit>()
    val eventSuccess = _eventSuccess.asLiveData()

    private val _eventPwdIndicatorStateChanged = SingleLiveEvent<Pair<Int, Boolean>>()

    /**
     * **Parameter**: Pair<Index, CheckState>
     *
     * Index에 있는 indicator의 check state가 변경되었을 때 호출됨.
     * 만약 Index가 [INDICATOR_INDEX_ALL]이면 모든 indicator를 지칭함
     */
    val eventPwdIndicatorStateChanged = _eventPwdIndicatorStateChanged.asLiveData()

    private val pwdMemory = PasswordMemory(6)


    init {
        pwdMemory.setOnFullListener(this)
    }

    override fun onPasswordInput(password: String) {
        _eventSuccess.call()
    }

    fun appendCode(digit: Char) {
        val length = pwdMemory.pushDigit(digit)
        if (length > 0) {
            _eventPwdIndicatorStateChanged.value = (length - 1) to true
        } else {
            _eventPwdIndicatorStateChanged.value = INDICATOR_INDEX_ALL to false
        }
    }

    fun removeCode() {
        val length = pwdMemory.popDigit()
        _eventPwdIndicatorStateChanged.value = length to false
    }

    companion object {
        const val INDICATOR_INDEX_ALL = -1
    }
}