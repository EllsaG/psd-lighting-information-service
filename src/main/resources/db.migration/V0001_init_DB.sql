create table light_information
(
    light_information_id int2 primary key,
    luminaire_model varchar(255) not null,
    lamp_model varchar(255) not null,
    amount_luminaires int2 not null,
    amount_lamps_in_one_luminaire int2 not null,
    one_lamp_power float4 not null,
    one_lamp_light_flux float4 not null,
    distance_between_lamp_rows float4 not null,
    distance_between_wall_and_first_lamp_row float4 not null,
    amount_luminaires_per_length int2 not null,
    amount_luminaires_per_width int2 not null,
    total_active_power float4 not null,
    total_reactive_power float4 not null,
    total_full_power float4 not null,
    electric_current float4 not null,
    electric_current_of_one_row_of_luminaires float4 not null,
    cos_f float4 not null,
    tg_f float4 not null
);

create table luminaire_selection
(
    luminaire_selection_id  int2 primary key,
    distance_between_lamp_rows float4 not null,
    distance_between_wall_and_first_lamp_row float4 not null,
    amount_luminaires_per_length int2 not null,
    amount_luminaires_per_width int2 not null,
    light_flux float4 not null,
    production_hall_height float4 not null,
    production_hall_length float4 not null,
    production_hall_width float4 not null
);