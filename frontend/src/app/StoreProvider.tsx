// 여기 뿐만이아니라 파이썬도 객체는 전부 파스칼을 사용을 한다. 파슼라을 존중한다.
// 객체는 객체에대한 정의를 파스칼이여서, 그래서 이러헥 하는거다.
// 근데 나머지, 객체 이외에는 파이썬은 밑줄(언더바) 스크립트는 -- / 자바는 카멜 형식이다.
'use client'
import { useRef } from 'react'
import { Provider } from 'react-redux'
import { makeStore, AppStore } from '../lib/store'

export default function StoreProvider({
                                          children
                                      }: {
    children: React.ReactNode
}) {
    const storeRef = useRef<AppStore>()
    if (!storeRef.current) {
        // Create the store instance the first time this renders
        storeRef.current = makeStore()
    }

    return <Provider store={storeRef.current}>{children}</Provider>
}