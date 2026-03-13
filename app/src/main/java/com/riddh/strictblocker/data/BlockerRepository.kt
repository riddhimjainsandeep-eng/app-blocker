package com.riddh.strictblocker.data

import kotlinx.coroutines.flow.Flow

class BlockerRepository(private val blockerDao: BlockerDao) {
    val allApps: Flow<List<BlockedApp>> = blockerDao.getAllApps()
    val allUrls: Flow<List<BlockedUrl>> = blockerDao.getAllUrls()
    val allKeywords: Flow<List<BlockedKeyword>> = blockerDao.getAllKeywords()

    suspend fun insertApp(app: BlockedApp) = blockerDao.insertApp(app)
    suspend fun deleteApp(app: BlockedApp) = blockerDao.deleteApp(app)

    suspend fun insertUrl(url: BlockedUrl) = blockerDao.insertUrl(url)
    suspend fun deleteUrl(url: BlockedUrl) = blockerDao.deleteUrl(url)

    suspend fun insertKeyword(keyword: BlockedKeyword) = blockerDao.insertKeyword(keyword)
    suspend fun deleteKeyword(keyword: BlockedKeyword) = blockerDao.deleteKeyword(keyword)
}
