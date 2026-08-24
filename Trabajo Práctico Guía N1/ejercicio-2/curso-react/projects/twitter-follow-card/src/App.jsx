import './App.css'
import { TwitterFollowCard } from './TwitterFollowCard'

export function App () {

    //pasar funciones como parametros porque son ciudadanos de primera clase
    //no modificar ni mutar una prop tienen que ser inmutables
    //const format = (userName) => `@${userName}`

    //componentes crean elementos y los elementos son los que renderiza react
    //const formattedUserName = <span>@{userName}</span> pasar elementos

    const users = [
        {
            userName:"midudev",
            name: 'Miguel Angel Durán',
            isFollowing: true
        },
        {
            userName:"sanbenito",
            name: 'Benito Antonio',
            isFollowing: true
        },
        {
            userName:"elonmusk",
            name: 'Elon Musk',
            isFollowing: false   
        },
        {
            userName:"BillGates",
            name: 'Bill Gates',
            isFollowing: false
        }
    ]

    //key es el id del elemento del array
    
    return (
        <section className='App'>
            {
                users.map(({ userName, name, isFollowing }) => (
                    <TwitterFollowCard
                        key={userName}
                        userName={userName}
                        initialIsFollowing={isFollowing}
                    >
                        {name}
                    </TwitterFollowCard>
                ))
            }
        </section>
    )
}