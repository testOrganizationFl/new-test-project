import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Headers
import java.io.IOException

data class InstagramResponse(
    val users: List<User>,
    val big_list: Boolean,
    val page_size: Int,
    val next_max_id: String?,
    val has_more: Boolean,
    val should_limit_list_of_followers: Boolean,
    val use_clickable_see_more: Boolean,
    val show_spam_follow_request_tab: Boolean,
    val status: String
)

data class User(
    val pk: String,
    val pk_id: String,
    val id: String,
    val full_name: String,
    val is_private: Boolean,
    val fbid_v2: String,
    val third_party_downloads_enabled: Int,
    val strong_id__: String,
    val profile_pic_id: String,
    val profile_pic_url: String,
    val is_verified: Boolean,
    val username: String,
    val has_anonymous_profile_picture: Boolean,
    val account_badges: List<String>,
    val latest_reel_media: Int
)

fun main() {
    val curlCommand = """
https://www.instagram.com/api/v1/friendships/214270335/followers/?count=12&search_surface=follow_list_page \
  -H 'accept: */*' \
  -H 'accept-language: en-GB,en;q=0.9,es-ES;q=0.8,es;q=0.7,en-US;q=0.6' \
  -H 'cookie: ig_did=62F0D3B1-47BE-4B8B-88B8-302FBAC9DD8D; fbm_124024574287414=base_domain=.instagram.com; ds_user_id=3432000151; ps_n=1; ps_l=1; mid=Zoy2XAAEAAFaC9-zne6YuU52dXqC; oo=v1; csrftoken=U4D3R8b7A8kzcYfZMU6xbHqAZOS92Oo0; dpr=1; datr=QliiZkIyAw5oy4NshULLkZbC; shbid="2684\0543432000151\0541754106007:01f735f5df1062a2333b8bc04dba2873fe95b7eeaeee0997d069ef70fea8ed2566628958"; shbts="1722570007\0543432000151\0541754106007:01f799e068954697d0799250c91b57e8a0b1715ebad11bddc17076478260e757375003c5"; ig_direct_region_hint="RVA\0543432000151\0541754211307:01f7041fe7a333111b8456b31f080217c1a7a6d06511a7f8c44c59e92af4ddfb78c33e36"; sessionid=3432000151%3AeOWn2t5FxITpFi%3A24%3AAYfeHsBK10JNoaJf9ctOoNmRWQNpUQ83v3ENoFoKvfRe; wd=917x823; fbsr_124024574287414=1IbKWgnvOSuB8ZRd_yNfeH_90d5f35E1FX0QBPTDIbU.eyJ1c2VyX2lkIjoiNzIxMDIyOTg2IiwiY29kZSI6IkFRRGE0dEVHSGo3UXZNcVVuZzJDOE5naWhfVDU3ODZxZEZleC11WWNuWTFfdlBGbkRVY2Q0ZHIyTmtPM1Z1VTZuOUVyRnplQ0YzSmxmV3VGLU55UVo1bjcwV1E5bkFaNFlhZm5PZzVta3lGcU55aVNtbVBQa3hiYTZucTBkYTZ5OEF6NkF3bDRGZUhrSGRGd19za0ZFT1BlNmhDUl81b0lacnE4czdvR0NaT3BrQzJqeGtoUXR3bVNINDZmMTd5ZXJZclk3amdoM0I2eFhOa1hFN2p1Y04xTUg0Uk1ZYXRqSHNmTk9nYlFoeFhGc0lZYnh6bDU1T2hUdUZ5ZkxnWEZ1elVuRWJzdWJmY1hfVk5jd3lyUnhpMzJGOThIRW5yVW9MZV94ellaYncyRFVNb21GMEIxSFhPWTBHNEx6aVY2SktnIiwib2F1dGhfdG9rZW4iOiJFQUFCd3pMaXhuallCTzVyQmV6dUhvQXRlelpBYmYxZFBFeDBaQndaQzBvRzRFaUJFQ2o0RzhJdVNicU1nZ285a2ZxVE1rS21xdnpidHVHSWRrNmFhdWpuMXdiaGRURXd2MWg2YjFaQUhaQXp0b0F5V3hMRHREUEVLUWJTTmhESjlTM1VzenNkMnJXaVpBMGZhNG82WFBNbkkyeW5kYjRUckV4TXUySzI0Q1RaQWt0SVg2WkI2Yk81RnJQZEVUWURhUEhaQmNzU0xxNDl3WkQiLCJhbGdvcml0aG0iOiJITUFDLVNIQTI1NiIsImlzc3VlZF9hdCI6MTcyMjc1MDM2OX0; fbsr_124024574287414=1IbKWgnvOSuB8ZRd_yNfeH_90d5f35E1FX0QBPTDIbU.eyJ1c2VyX2lkIjoiNzIxMDIyOTg2IiwiY29kZSI6IkFRRGE0dEVHSGo3UXZNcVVuZzJDOE5naWhfVDU3ODZxZEZleC11WWNuWTFfdlBGbkRVY2Q0ZHIyTmtPM1Z1VTZuOUVyRnplQ0YzSmxmV3VGLU55UVo1bjcwV1E5bkFaNFlhZm5PZzVta3lGcU55aVNtbVBQa3hiYTZucTBkYTZ5OEF6NkF3bDRGZUhrSGRGd19za0ZFT1BlNmhDUl81b0lacnE4czdvR0NaT3BrQzJqeGtoUXR3bVNINDZmMTd5ZXJZclk3amdoM0I2eFhOa1hFN2p1Y04xTUg0Uk1ZYXRqSHNmTk9nYlFoeFhGc0lZYnh6bDU1T2hUdUZ5ZkxnWEZ1elVuRWJzdWJmY1hfVk5jd3lyUnhpMzJGOThIRW5yVW9MZV94ellaYncyRFVNb21GMEIxSFhPWTBHNEx6aVY2SktnIiwib2F1dGhfdG9rZW4iOiJFQUFCd3pMaXhuallCTzVyQmV6dUhvQXRlelpBYmYxZFBFeDBaQndaQzBvRzRFaUJFQ2o0RzhJdVNicU1nZ285a2ZxVE1rS21xdnpidHVHSWRrNmFhdWpuMXdiaGRURXd2MWg2YjFaQUhaQXp0b0F5V3hMRHREUEVLUWJTTmhESjlTM1VzenNkMnJXaVpBMGZhNG82WFBNbkkyeW5kYjRUckV4TXUySzI0Q1RaQWt0SVg2WkI2Yk81RnJQZEVUWURhUEhaQmNzU0xxNDl3WkQiLCJhbGdvcml0aG0iOiJITUFDLVNIQTI1NiIsImlzc3VlZF9hdCI6MTcyMjc1MDM2OX0; rur="EAG\0543432000151\0541754286546:01f7dd4d33adace4c8cdb1c9d2bf6eb7346e8b46668b593018d6f2d9bcf6ec8db62354cf"' \
  -H 'priority: u=1, i' \
  -H 'referer: https://www.instagram.com/deneidacr/followers/' \
  -H 'sec-ch-prefers-color-scheme: dark' \
  -H 'sec-ch-ua: "Not/A)Brand";v="8", "Chromium";v="126", "Google Chrome";v="126"' \
  -H 'sec-ch-ua-full-version-list: "Not/A)Brand";v="8.0.0.0", "Chromium";v="126.0.6478.182", "Google Chrome";v="126.0.6478.182"' \
  -H 'sec-ch-ua-mobile: ?0' \
  -H 'sec-ch-ua-model: ""' \
  -H 'sec-ch-ua-platform: "macOS"' \
  -H 'sec-ch-ua-platform-version: "14.4.1"' \
  -H 'sec-fetch-dest: empty' \
  -H 'sec-fetch-mode: cors' \
  -H 'sec-fetch-site: same-origin' \
  -H 'user-agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36' \
  -H 'x-asbd-id: 129477' \
  -H 'x-csrftoken: U4D3R8b7A8kzcYfZMU6xbHqAZOS92Oo0' \
  -H 'x-ig-app-id: 936619743392459' \
  -H 'x-ig-www-claim: hmac.AR3lbLND3PSRc_pzbTJkBzPRU4HTDWwIZt6CM2YZX12uQ_0o' \
  -H 'x-requested-with: XMLHttpRequest'


    """.trimIndent()

    val client = OkHttpClient()
    val gson = Gson()
    var maxId: String? = null

    val url = curlCommand.substringBefore(" \\").trim()
    val headers = extractHeaders(curlCommand)

    var loop = true;
    do {
        val fullUrl = buildUrl(url, maxId)
        val request = Request.Builder()
            .url(fullUrl)
            .headers(headers)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val responseBody = response.body?.string()
            if (responseBody != null) {
                val instagramResponse = gson.fromJson(responseBody, InstagramResponse::class.java)

                // Extract and print the usernames
                for (user in instagramResponse.users) {
                    println(user.username)
                }

                // Update maxId for the next request
                maxId = instagramResponse.next_max_id

                // Check if there are more pages
                loop = instagramResponse.has_more
            }
        }
    } while (loop)
}

fun extractHeaders(curlCommand: String): Headers {
    val headerLines = curlCommand.split("\\").drop(1)
    val headersBuilder = Headers.Builder()

    for (line in headerLines) {
        if (line.trim().startsWith("-H")) {
            val header = line.substringAfter("-H '").substringBefore("'").split(": ", limit = 2)
            if (header.size == 2) {
                headersBuilder.add(header[0], header[1])
            }
        }
    }

    return headersBuilder.build()
}

fun buildUrl(baseUrl: String, maxId: String?): String {
    return if (maxId != null) {
        "$baseUrl&max_id=$maxId"
    } else {
        baseUrl
    }
}
