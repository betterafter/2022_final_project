package com.example.kuroutine

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

// hilt라는 라이브러리를 쓰기 위한 어노테이션(@)이에요. hilt는 의존성 주입을 위한 라이브러리입니다.
// 밑에 보시면 @Inject constructor라는 것이 있는데, 이걸 왜 쓰는지는 아래를 봐주세요.
@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
}


/*

의존성 주입이란??????

아래와 같은 클래스 2개와 main 함수가 있어요. (수도 코드로 대충 짤게요)
class Dog {
    int color;
    int leg;
    int age;
    int price;

    constructor(color, leg, age, price)
}

class DogShop {
    var list: List<Dog>;

    void init() {

    }
}

void main() {
    DogShop.init()
}

DogShop에 강아지를 추가하는 초기화 함수를 구현해 볼까요?

--->
class DogShop {
    var list: List<Dog>;

    void init() {
        list.add(Dog(purple, 4, 10, 100000)
    }
}

그런데 생각해보니 강아지의 다리 속성은 그다지 필요가 없네요. 갑자기 'leg' 속성을 빼고 싶어요.

--->
class Dog {
    int color;
    int age;
    int price;

    constructor(color, age, price)
}

이렇게 되면 DogShop 클래스도 수정을 해야겠네요! 다리 속성이 생성자에서 빠졌으니까요.

--->
class DogShop {
    var list: List<Dog>;

    void init() {
        list.add(Dog(purple, 10, 100000)
    }
}



특정 클래스(Dog)에서 수정이 이루어졌을 때, 다른 클래스(DogShop)에 해당 클래스의 객체를 직접 생성해서 관리하게 되면,
해당 클래스의 변경이 이루어질 때 마다 그 클래스를 불러다쓰는 모든 클래스를 수정해야 돼요. 위에선 DogShop 안의 Dog만 수정하면 되지만,
만약 Dog를 쓰는 파일이 1억개라면? 그걸 다 수정하기는 버겁겠지요. 그래서 고안한 방법이 직접 만드는게 아니라 아예 밖에서 불러다 쓰는 방식입니다.
즉, 파라미터로 념겨주는 방식이죠!!

--->
class DogShop {
    var list: List<Dog>;

    // Dog를 밖에서 불러와서 안에 넣어줍니다.
    void init(Dog dog) {
        list.add(dog)
    }
}

void main() {
    DogShop().init(Dog(purple, 10, 100000))
}

---------------------------------------------------------------------------------------------------------
(여기서부터 조금 어려운 개념인데, 한번 슥 읽어보세요. 이해가 되지 않을 수도 있습니다.)
질문이 있을 수도 있어요. "그럼 클래스가 변경될 때마다 밖에서 들고오는, 즉 main함수가 변경되어야 하는데, 결국 작업량은 별 차이 없지 않나요?"
가령 서버에서

{
    name: 신기열,
    age: 27,
    rank: VVIP,
}

라는 데이터를 들고 온다면, 앱은 저 데이터를

class Customer(
    String? name,
    int? age,
    String? rank

    constructor()
)

로 가공해서 뽑아내게끔 클래스를 만들고, 만약 여러 개의 클래스에서 저 데이터를 사용한다면, 아래의 2가지 방식 중 어떤게 수정하기 더 쉬울까요??

--------------------------------- 1번 방법 -------------------------------------

void main() {
    MainPage().init()
    EventPage().init()
}

class MainPage {
    var list: List<Customer>;

    void init() {
        List<Customer> dataList = [
            Customer("기열1", 27, ""VVIP"),
            Customer("기열2", 27, "GOLD"),
            Customer("기열3", 27, "GOLD"),
            Customer("기열4", 27, "GOLD"),
            Customer("기열5", 27, "GOLD"),
        ]
        list.addAll(dataList)
    }
}


class EventPage {
    var list: List<Customer>;

      void init() {
        List<Customer> dataList = [
            Customer("기열1", 27, ""VVIP"),
            Customer("기열2", 27, "GOLD"),
            Customer("기열3", 27, "GOLD"),
            Customer("기열4", 27, "GOLD"),
            Customer("기열5", 27, "GOLD"),
        ]
        list.addAll(dataList)
    }
}


--------------------------------- 2번 방법 -------------------------------------

void main() {
    List<Customer> dataList = [
        Customer("기열1", 27, ""VVIP"),
        Customer("기열2", 27, "GOLD"),
        Customer("기열3", 27, "GOLD"),
        Customer("기열4", 27, "GOLD"),
        Customer("기열5", 27, "GOLD"),
    ]

    MainPage().init(dataList)
    EventPage().init(dataList)
}

class MainPage {
    var list: List<Customer>;

    void init(List<Customer> list) {
        list.addAll(list)
    }
}


class EventPage {
    var list: List<Customer>;

    void init(List<Customer> list) {
        list.addAll(list)
    }
}


1번 방법은 직접 다 수정해야 되고, 2번은 main만 수정하면 되겠죠? 구현만 조금 잘한다면 작업량을 크게 줄일 수 있고,
결론적으로 main에서 생성해서 파라미터로 넘기는 "의존성 주입" 방식이 더 효율적이게 됩니다. 이게 "의존성 주입"의 개념이에요.
맨 위에서 ViewModel에 @Inject constructor() 을 쓰는 이유가 바로 파라미터로 데이터를 넘기기 위해서 쓰는 것이에요.
hilt는 대신 main의

 List<Customer> dataList = [
        Customer("기열1", 27, ""VVIP"),
        Customer("기열2", 27, "GOLD"),
        Customer("기열3", 27, "GOLD"),
        Customer("기열4", 27, "GOLD"),
        Customer("기열5", 27, "GOLD"),
    ]

이와 같이 데이터를 직접 생성하는 작업을 자동적으로 만들어서 넘겨주는 편한 라이브러리이구요.
(내부적으로 저걸 생성해서 넘기는 원리는 너무 복잡하니까 넘어갑시다 ㅋㅋ)
 */