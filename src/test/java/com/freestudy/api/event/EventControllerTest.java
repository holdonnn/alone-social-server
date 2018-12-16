package com.freestudy.api.event;

import com.freestudy.api.BaseControllerTest;
import com.freestudy.api.DisplayName;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class EventControllerTest extends BaseControllerTest {

  @Autowired
  private EventRepository eventRepository;

  @Test
  @DisplayName("정상적으로 이벤트를 사용할 때")
  public void createEvent() throws Exception {

    EventDto event = EventDto.builder()
            .name("SpringBootIsFun")
            .description("Rest")
            .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 11, 0, 0))
            .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 11, 0, 0))
            .beginEventDateTime(LocalDateTime.of(2018, 11, 11, 0, 0))
            .endEventDateTime(LocalDateTime.of(2018, 11, 11, 0, 0))
            .basePrice(1000)
            .maxPrice(10000)
            .limitOfEnrollment(5)
            .location("낙성대")
            .build();

    mockMvc
            .perform(
                    post("/api/events/")
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .accept(MediaTypes.HAL_JSON)
                            .content(objectMapper.writeValueAsString(event))
            )
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("id").isNumber())
            .andExpect(header().exists(HttpHeaders.LOCATION))
            .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
            .andDo(
                    document("create-event",
                            links(
                                    linkWithRel("self").description("link to self"),
                                    linkWithRel("query-events").description("query")
                            ),
                            requestHeaders(
                                    headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                    headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                            ),
                            requestFields(
                                    fieldWithPath("name").description("Name of new event"),
                                    fieldWithPath("description").description("Description of new event"),
                                    fieldWithPath("location").description("Location of new event"),
                                    fieldWithPath("beginEnrollmentDateTime").description("Description of new event"),
                                    fieldWithPath("closeEnrollmentDateTime").description("모임 등록 마감 시간"),
                                    fieldWithPath("beginEventDateTime").description("beginEventDateTime"),
                                    fieldWithPath("endEventDateTime").description("endEventDateTime"),
                                    fieldWithPath("basePrice").description("basePrice"),
                                    fieldWithPath("maxPrice").description("maxPrice"),
                                    fieldWithPath("limitOfEnrollment").description("limitOfEnrollment")
                            ),
                            relaxedResponseFields(
                                    fieldWithPath("id").description("event id")
                            )
                    )
            );

  }

  @Test
  @DisplayName("입력값이 없는 경우에")
  public void createEvent_Bad_Request_Empty_Input() throws Exception {

    EventDto eventDto = EventDto.builder().build();

    mockMvc
            .perform(
                    post("/api/events/")
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .accept(MediaTypes.HAL_JSON)
                            .content(objectMapper.writeValueAsString(eventDto))
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("_links.index").exists());
  }

  @Test
  @DisplayName("입력값이 있으나 잘못되 경우일 때, 이벤트 시작일은 종료일보다 이전이여야 한다.")
  public void createEvent_Bad_Request_Invalid_Input() throws Exception {

    EventDto eventDto = EventDto.builder()
            .name("SpringBootIsFun")
            .description("Rest")
            .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 11, 0, 0))
            .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 11, 0, 0))
            .beginEventDateTime(LocalDateTime.of(2018, 11, 15, 0, 0))
            .endEventDateTime(LocalDateTime.of(2018, 11, 10, 0, 0))
            .basePrice(1000)
            .maxPrice(10000)
            .limitOfEnrollment(5)
            .location("낙성대")
            .build();

    mockMvc
            .perform(
                    post("/api/events/")
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .accept(MediaTypes.HAL_JSON)
                            .content(objectMapper.writeValueAsString(eventDto))
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("_links.index").exists());
  }

  @Test
  @DisplayName("입력값이 있으나 잘못되 경우일 때, 이에 대한 정보가 응답에 담겨야한다.")
  public void createEvent_Bad_Request_Invalid_Input_With_Response() throws Exception {

    EventDto eventDto = EventDto.builder()
            .name("SpringBootIsFun")
            .description("Rest")
            .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 11, 0, 0))
            .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 11, 0, 0))
            .beginEventDateTime(LocalDateTime.of(2018, 11, 15, 0, 0))
            .endEventDateTime(LocalDateTime.of(2018, 11, 10, 0, 0))
            .basePrice(1000)
            .maxPrice(10000)
            .limitOfEnrollment(5)
            .location("낙성대")
            .build();

    mockMvc
            .perform(
                    post("/api/events/")
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .accept(MediaTypes.HAL_JSON)
                            .content(objectMapper.writeValueAsString(eventDto))
            )
            .andDo(print())
            .andExpect(jsonPath("content[0].objectName").exists())
            .andExpect(jsonPath("content[0].defaultMessage").exists())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("_links.index").exists());
  }

  @Test
  @DisplayName("30개의 이벤트를 10개씩 두번째 페이지 조회 이벤트 이름순 정렬")
  public void queryEvents() throws Exception {
    // Given
    IntStream.range(0, 30).forEach(this::generateEvent);

    // When
    var perform = this.mockMvc.perform(
            get("/api/events")
                    .param("page", "1")
                    .param("size", "10")
                    .param("sort", "name,desc")
    );

    // Then
    perform
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("page").exists())
            .andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
            .andExpect(jsonPath("_links.self").exists())
            .andExpect(jsonPath("_links.profile").exists())
            .andDo(document("query-events"));
  }

  @Test
  @DisplayName("기존 이벤트 하나 조회")
  public void getEvent() throws Exception {
    // Given
    Event event = this.generateEvent(100);

    // When
    var perform = this.mockMvc.perform(get("/api/events/{id}", event.getId()));

    // Then
    perform.andExpect(status().isOk());
    perform.andExpect(jsonPath("id").exists());
    perform.andExpect(jsonPath("name").exists());
    perform.andExpect(jsonPath("description").exists());
    perform.andExpect(jsonPath("_links.self").exists());
    perform.andExpect(jsonPath("_links.profile").exists());
    perform.andDo(document("get-an-event"));
  }


  @Test
  @DisplayName("기존 이벤트 하나 조회, 이벤트가 없을 때")
  public void getEvent_Not_Found() throws Exception {
    // When
    var perform = this.mockMvc.perform(get("/api/events/{id}", 12345));

    // Then
    perform.andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("이벤트 수정")
  public void updateEvent() throws Exception {
    // Given
    Event event = generateEvent(200);
    EventDto eventDto = this.modelMapper.map(event, EventDto.class);
    String updatedName = "updated event";
    eventDto.setName(updatedName);

    // When
    var perform = this.mockMvc.perform(
            put("/api/events/{id}", event.getId())
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(this.objectMapper.writeValueAsString(eventDto))
    );

    perform.andDo(print());
    perform.andExpect(status().isOk());
    perform.andExpect(jsonPath("name").value(updatedName));
  }

  @Test
  @DisplayName("이벤트 수정, 없는 이벤트에 대해서")
  public void updateEvent_Not_found() throws Exception {
    // Given
    Event event = generateEvent(123);
    EventDto eventDto = this.modelMapper.map(event, EventDto.class);

    // When
    var perform = this.mockMvc.perform(
            put("/api/events/999999999")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(this.objectMapper.writeValueAsString(eventDto))
    );

    perform.andDo(print());
    perform.andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("이벤트 수정, 입력값이 잘못된 경우")
  public void updateEvent_Invalid_Input() throws Exception {
    // Given
    Event event = generateEvent(200);
    EventDto eventDto = this.modelMapper.map(event, EventDto.class);
    eventDto.setEndEventDateTime(LocalDateTime.of(2018, 11, 11, 0, 0));
    eventDto.setBeginEventDateTime(LocalDateTime.of(2018, 11, 15, 0, 0));

    // When
    var perform = this.mockMvc.perform(
            put("/api/events/{id}", event.getId())
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(this.objectMapper.writeValueAsString(eventDto))
    );

    // Then
    perform.andDo(print());
    perform.andExpect(status().isBadRequest());
  }

  private Event generateEvent(int i) {
    Event event = Event.builder()
            .name("event" + i)
            .description("Rest")
            .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 11, 0, 0))
            .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 11, 0, 0))
            .beginEventDateTime(LocalDateTime.of(2018, 11, 11, 0, 0))
            .endEventDateTime(LocalDateTime.of(2018, 11, 11, 0, 0))
            .basePrice(1000)
            .maxPrice(10000)
            .limitOfEnrollment(5)
            .location("낙성대")
            .build();
    return this.eventRepository.save(event);
  }

}
