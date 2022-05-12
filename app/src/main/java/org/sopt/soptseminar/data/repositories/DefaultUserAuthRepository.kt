package org.sopt.soptseminar.data.repositories

import org.sopt.soptseminar.data.service.SoptService
import org.sopt.soptseminar.data.models.RequestSignIn
import org.sopt.soptseminar.domain.UserAuthRepository
import org.sopt.soptseminar.models.UserInfo
import org.sopt.soptseminar.modules.datastore.UserPreferenceRepository
import javax.inject.Inject

class DefaultUserAuthRepository @Inject constructor(
    private val soptService: SoptService,
    private val userPreferenceRepository: UserPreferenceRepository
) : UserAuthRepository {
    override suspend fun signIn(email: String, password: String): Boolean {
        runCatching {
            soptService.postSignIn(RequestSignIn(email, password))
        }.fold({
            val data = it.body()?.data ?: return false // TODO
            userPreferenceRepository.setUserPreference(
                UserInfo(
                    name = data.name,
                    age = 24,
                    mbti = "ISFP",
                    university = "성신여대",
                    major = "컴퓨터공학과",
                    email = "cyjin6@naver.com"
                )
            )
            return true
        }, {
            it.printStackTrace()
            return false
        })
    }

    override suspend fun signUp(name: String, email: String, password: String): Boolean {
        runCatching {
            soptService.postSignIn(RequestSignIn(email, password))
        }.fold({
            return true
        }, {
            it.printStackTrace()
            return false
        })
    }
}