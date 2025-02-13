/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 */

export default {
	description_i18n: {
		en_US: 'Boost contents matching all the words in the search phrase',
	},
	elementDefinition: {
		category: 'boost',
		configuration: {
			queryConfiguration: {
				queryEntries: [
					{
						clauses: [
							{
								context: 'query',
								occur: 'should',
								query: {
									multi_match: {
										boost: '${configuration.boost}',
										fields: '${configuration.fields}',
										operator: 'and',
										query: '${configuration.keywords}',
										type: '${configuration.type}',
									},
								},
							},
						],
					},
				],
			},
		},
		icon: 'thumbs-up',
		uiConfiguration: {
			fieldSets: [
				{
					fields: [
						{
							defaultValue: [
								{
									boost: 2.0,
									field: 'localized_title',
									locale: '${context.language_id}',
								},
								{
									boost: 1.0,
									field: 'content',
									locale: '${context.language_id}',
								},
							],
							label: 'Field',
							name: 'fields',
							type: 'fieldMappingList',
							typeOptions: {
								boost: true,
							},
						},
						{
							defaultValue: 'best_fields',
							label: 'Match Type',
							name: 'type',
							type: 'select',
							typeOptions: {
								options: [
									{
										label: 'Best Fields',
										value: 'best_fields',
									},
									{
										label: 'Most Fields',
										value: 'most_fields',
									},
									{
										label: 'Cross Fields',
										value: 'cross_fields',
									},
									{
										label: 'Phrase',
										value: 'phrase',
									},
									{
										label: 'Phrase Prefix',
										value: 'phrase_prefix',
									},
									{
										label: 'Boolean Prefix',
										value: 'bool_prefix',
									},
								],
							},
						},
						{
							defaultValue: 10,
							label: 'Boost',
							name: 'boost',
							type: 'number',
							typeOptions: {
								min: 0,
							},
						},
						{
							helpText:
								'If this is set, the search terms entered in the search bar will be replaced by this value.',
							label: 'Text to Match',
							name: 'keywords',
							type: 'keywords',
							typeOptions: {
								required: 'false',
							},
						},
					],
				},
			],
		},
	},
	title_i18n: {
		en_US: 'Boost All Keywords Match',
	},
};
