package co.lopun.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.reactor.asCoroutineDispatcher
import reactor.core.scheduler.Schedulers

// 필요에 따라서 Dispatcher들을 프로젝트별로 나눠도 되고 IO, COMPUTE 외의 다른 Dispatcher를 만들어도 된다.
enum class Dispatchers(val dispatcher: CoroutineDispatcher) {
  IO(Schedulers.newBoundedElastic(10, 100_000, "lopun-io").asCoroutineDispatcher()),
  COMPUTE(Schedulers.parallel().asCoroutineDispatcher())
}
